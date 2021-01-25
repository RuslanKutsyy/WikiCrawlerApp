package com.smule.wikicrawler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WikiResponseDto {
    @JsonProperty("parse")
    private final WikipediaArticleDto parse;

    public WikiResponseDto() {
        this.parse = new WikipediaArticleDto();
    }

    public WikipediaArticleDto getParse() {
        return parse;
    }
}
