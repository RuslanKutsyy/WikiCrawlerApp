package com.smule.wikicrawler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smule.wikicrawler.model.Request;

import java.util.List;

public class RequestDto {
    @JsonProperty("id")
    private int id;
    private String keyword;
    private String status;
    private List<ResultDto> results;

    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultDto> getResults() {
        return results;
    }

    public void setResults(List<ResultDto> results) {
        this.results = results;
    }
}
