package com.smule.wikicrawler.dao;

import com.smule.wikicrawler.dto.RequestDto;
import com.smule.wikicrawler.model.Request;
import com.smule.wikicrawler.model.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WikiArticleResultService {
    private WikiArticleResultRepository wikiArticleRepository;
    private RequestService requestService;

    public WikiArticleResultService(WikiArticleResultRepository wikiArticleRepository, RequestService requestService){
        this.wikiArticleRepository = wikiArticleRepository;
        this.requestService = requestService;
    }

    public List<Result> getRequestResults(int reqID) {
        List<Result> data = new ArrayList<>();
        wikiArticleRepository.findAll().forEach(result -> data.add(result));

        List<Result> results = data.stream().filter(result -> result.GetRequestId() == reqID).collect(Collectors.toList());

        return results;
    }

    public void addResultsToDB(int reqID, String articleName, String articleLink) {
        Request request = requestService.getRequestById(reqID);
        Result result = new Result();
        result.SetArticleName(articleName);
        result.SetArticleUrl(articleLink);
        wikiArticleRepository.save(result);
    }
}
