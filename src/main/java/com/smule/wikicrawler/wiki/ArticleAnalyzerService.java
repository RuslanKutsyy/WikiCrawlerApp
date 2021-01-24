package com.smule.wikicrawler.wiki;

import com.smule.wikicrawler.database.WikiArticleResultService;
import com.smule.wikicrawler.database.WikiRequestService;
import com.smule.wikicrawler.http.WikiWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

@Service
public class ArticleAnalyzerService {
    @Qualifier("getThreadPool")
    @Autowired
    private Executor threadPool;
    @Autowired
    private WikiRequestService wikiRequestService;
    @Autowired
    private WikiArticleResultService wikiArticleResultService;
    @Autowired
    WikiWebService webService;

    private final DataParser articleParser;

    public ArticleAnalyzerService() {
        this.articleParser = new DataParser();
    }

    public void submitNewRequestForAnalysis(int id, String keyword) {
        final String processStatus = "Processing";
        final String done = "Completed";
        final String failed = "Failed";
        wikiRequestService.changeRequestStatus(id, processStatus);
        WikipediaArticle article = webService.getWikiArticleContent(keyword);

        if (article != null){
            analyze(article, id, keyword);
            wikiRequestService.changeRequestStatus(id, done);
        } else {
            wikiRequestService.changeRequestStatus(id, failed);
        }
    }

    private void analyze(WikipediaArticle article, int id, String keyword){
        final int matcherGroupID = 2;
        final String patternToGetAllArticlesTitles = "<a href=\"/wiki(/(.*?)\")";
        final String articleURL = "https://en.wikipedia.org/wiki/";
        List<String> parsedData = articleParser.parseData(article.getText().get("*"), patternToGetAllArticlesTitles, matcherGroupID);
        for (String articleName : parsedData) {
            threadPool.execute(() -> {
                WikipediaArticle innerArticle = webService.getWikiArticleContent(articleName);
                if (innerArticle != null) {
                    List<String> linkList = articleParser.parseData(innerArticle.getText().get("*"), patternToGetAllArticlesTitles, matcherGroupID);
                    for (String innerArticleLink : linkList) {
                        if (innerArticleLink.equalsIgnoreCase(keyword)) {
                            wikiArticleResultService.addResultsToDB(id, articleName, articleURL + articleName);
                            break;
                        }
                    }
                }
            });
        }
    }
}
