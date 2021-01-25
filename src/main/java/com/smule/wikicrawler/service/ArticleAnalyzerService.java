package com.smule.wikicrawler.service;

import com.smule.wikicrawler.dao.ResultService;
import com.smule.wikicrawler.dao.RequestService;
import com.smule.wikicrawler.dto.WikipediaArticleDto;
import com.smule.wikicrawler.model.Request;
import com.smule.wikicrawler.model.Result;
import com.smule.wikicrawler.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ArticleAnalyzerService {
    @Qualifier("getThreadPool")
    @Autowired
    private ExecutorService threadPool;
    private RequestService requestService;
    private ResultService resultService;
    private WikiWebService webService;
    private WikiDataParserService articleParser;
    private final String articleURL = "https://en.wikipedia.org/wiki/";
    final int matcherGroupID = 2;
    final String patternToGetAllArticlesTitles = "<a href=\"/wiki(/(.*?)\")";
    private final Object lock = new Object();

    public ArticleAnalyzerService(RequestService requestService, ResultService resultService, WikiWebService webService, WikiDataParserService articleParser) {
        this.requestService = requestService;
        this.resultService = resultService;
        this.webService = webService;
        this.articleParser = articleParser;
    }

    public void submitNewRequest(int id, String keyword) {
        requestService.updateRequestStatus(id, Status.Pending);
        WikipediaArticleDto article = webService.getWikiArticleContent(keyword);

        if (article != null){
            analyzeArticle(article, id, keyword);
        } else {
            requestService.updateRequestStatus(id, Status.Failed);
        }
    }

    private void analyzeArticle(WikipediaArticleDto article, int requestId, String keyword){
        requestService.updateRequestStatus(requestId, Status.Processing);
        Request currentRequest = requestService.getRequestById(requestId);
        List<String> parsedData = articleParser.ParseData(article.getText().get("*"), patternToGetAllArticlesTitles, matcherGroupID);

        try {
            analyzeParsedArticleData(parsedData, keyword, currentRequest);
        } catch (InterruptedException | ExecutionException ex) {
            ex.getStackTrace();
        }

        requestService.updateRequestStatus(requestId, Status.Completed);
    }

    private void analyzeParsedArticleData(List<String> parsedData, String keyword, Request currentRequest) throws ExecutionException, InterruptedException {
        List<Future> tasks = new ArrayList<>();
        for (String articleName : parsedData) {
            tasks.add(threadPool.submit(() -> {
                WikipediaArticleDto innerArticle = webService.getWikiArticleContent(articleName);
                if (innerArticle != null) {
                    List<String> linkList = articleParser.ParseData(innerArticle.getText().get("*"), patternToGetAllArticlesTitles, matcherGroupID);
                    for (String innerArticleLink : linkList) {
                        if (innerArticleLink.equalsIgnoreCase(keyword)) {
                            Result result = new Result(articleName.replaceAll("_", " "), articleURL + articleName);
                            currentRequest.addResult(result);
                            try {
                                resultService.addNewResult(result);
                            } catch (SQLException ex) {
                                ex.getStackTrace();
                            }
                            break;
                        }
                    }
                }
            }));
        }

        for (Future task : tasks){
            task.get();
        }

        requestService.updateRequestStatus(currentRequest.getId(), Status.Completed);
    }
}
