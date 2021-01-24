package com.smule.wikicrawler.database;

import com.smule.wikicrawler.wiki.RequestResult;
import com.smule.wikicrawler.wiki.WikiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WikiArticleResultService {
    @Autowired
    WikiArticleResultRepository wikiArticleRepository;
    @Autowired
    WikiRequestService wikiRequestService;

    public List<RequestResult> getRequestResults(int reqID) {
        List<RequestResult> data = new ArrayList<>();
        wikiArticleRepository.findAll().forEach(requestResult -> data.add(requestResult));

        List<RequestResult> results = data.stream().filter(requestResult -> requestResult.getRequestID() == reqID).collect(Collectors.toList());

        return results;
    }

    public void addResultsToDB(int reqID, String articleName, String articleLink) {
        WikiRequest request = wikiRequestService.getRequestById(reqID);
        RequestResult result = new RequestResult();
        result.setRequest(request);
        result.setArticleName(articleName);
        result.setArticleLink(articleLink);
        wikiArticleRepository.save(result);
    }
}
