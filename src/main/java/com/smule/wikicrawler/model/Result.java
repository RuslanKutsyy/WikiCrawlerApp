package com.smule.wikicrawler.model;

import javax.persistence.*;

@Entity
@Table(name = "Results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "ArticleName")
    private String articleName;

    @Column(name = "ArticleURL")
    private String articleUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    private Request request;

    public Result() {
    }

    public Result(Request request, String articleName, String articleUrl) {
        this.request = request;
        this.articleName = articleName;
        this.articleUrl = articleUrl;
    }

    public Request getRequest() {
        return request;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
