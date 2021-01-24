package com.smule.wikicrawler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smule.wikicrawler.dto.WikipediaArticle;

public class WikiResponseDto {
    @JsonProperty("parse")
    private final WikipediaArticle parse;

    public WikiResponseDto() {
        this.parse = new WikipediaArticle();
    }

    public WikipediaArticle getParse() {
        return parse;
    }
}
