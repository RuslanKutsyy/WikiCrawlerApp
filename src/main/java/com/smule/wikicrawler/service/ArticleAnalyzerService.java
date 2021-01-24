package com.smule.wikicrawler.service;

import com.smule.wikicrawler.dao.ResultService;
import com.smule.wikicrawler.dao.RequestService;
import com.smule.wikicrawler.dto.WikipediaArticle;
import com.smule.wikicrawler.model.Request;
import com.smule.wikicrawler.model.Result;
import com.smule.wikicrawler.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class ArticleAnalyzerService {
    @Qualifier("getThreadPool")
    @Autowired
    private Executor threadPool;
    private RequestService requestService;
    private ResultService resultService;
    private WikiWebService webService;
    private WikiDataParserService articleParser;
    private final String articleURL = "https://en.wikipedia.org/wiki/";
    final int matcherGroupID = 2;
    final String patternToGetAllArticlesTitles = "<a href=\"/wiki(/(.*?)\")";

    public ArticleAnalyzerService(RequestService requestService, ResultService resultService, WikiWebService webService, WikiDataParserService articleParser) {
        this.requestService = requestService;
        this.resultService = resultService;
        this.webService = webService;
        this.articleParser = articleParser;
    }

    public void submitNewRequest(int id, String keyword) {
        requestService.updateRequestStatus(id, Status.Pending);
        WikipediaArticle article = webService.getWikiArticleContent(keyword);

        if (article != null){
            analyzeArticle(article, id, keyword);
            requestService.updateRequestStatus(id, Status.Completed);
        } else {
            requestService.updateRequestStatus(id, Status.Failed);
        }
    }

    private void analyzeArticle(WikipediaArticle article, int requestId, String keyword){
        requestService.updateRequestStatus(requestId, Status.Processing);
        Request currentRequest = requestService.getRequestById(requestId);
        List<String> parsedData = articleParser.ParseData(article.getText().get("*"), patternToGetAllArticlesTitles, matcherGroupID);

        for (String articleName : parsedData) {
            threadPool.execute(() -> {
                analyzeInnerArticle(currentRequest, keyword, articleName);
            });
        }
    }

    private void analyzeInnerArticle(Request request, String keyword, String articleName){
        WikipediaArticle innerArticle = webService.getWikiArticleContent(articleName);
        if (innerArticle != null) {
            List<String> linkList = articleParser.ParseData(innerArticle.getText().get("*"), patternToGetAllArticlesTitles, matcherGroupID);
            for (String innerArticleLink : linkList) {
                if (innerArticleLink.equalsIgnoreCase(keyword)) {
                    try {
                        Result result = resultService.addNewResult(request, articleName, articleURL + articleName);
                        request.addResult(result);
                    } catch (SQLException ex) {
                        ex.getStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
