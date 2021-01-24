package com.smule.wikicrawler.wiki;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class WikipediaArticle {
    @JsonProperty("title")
    private String title;
    @JsonProperty("pageid")
    private int pageID;
    @JsonProperty("text")
    private Map<String, String> text;

    public WikipediaArticle() {
        this.text = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public Map<String, String> getText() {
        return text;
    }
}
