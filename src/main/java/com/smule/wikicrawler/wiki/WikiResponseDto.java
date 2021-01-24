package com.smule.wikicrawler.wiki;

import com.fasterxml.jackson.annotation.JsonProperty;

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
