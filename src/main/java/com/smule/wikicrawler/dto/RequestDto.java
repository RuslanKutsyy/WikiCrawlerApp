package com.smule.wikicrawler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smule.wikicrawler.model.Request;

public class RequestDto {
    @JsonProperty("id")
    private Integer id;
    private String keyword;
    private String status;

    public RequestDto(){}

    public RequestDto(Integer id, String keyword, String status){
        this.id = id;
        this.keyword = keyword;
        this.status = status;
    }

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
}
