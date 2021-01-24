package com.smule.wikicrawler.dao;

import com.smule.wikicrawler.model.Request;
import com.smule.wikicrawler.model.Result;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {
    private ResultRepository wikiArticleRepository;

    public ResultService(ResultRepository wikiArticleRepository, RequestService requestService){
        this.wikiArticleRepository = wikiArticleRepository;
    }

    public List<Result> getRequestResults(int reqID) {
        List<Result> data = new ArrayList<>();
        wikiArticleRepository.findAll().forEach(result -> data.add(result));

        List<Result> results = data.stream().filter(result -> result.getRequest().getId() == reqID).collect(Collectors.toList());

        return results;
    }

    public Result addNewResult(Request request, String articleName, String articleUrl) throws SQLException {
        Result result = new Result(request, articleName, articleUrl);
        wikiArticleRepository.save(result);

        return result;
    }
}
