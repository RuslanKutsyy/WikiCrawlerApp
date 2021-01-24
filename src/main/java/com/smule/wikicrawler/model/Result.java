package com.smule.wikicrawler.wiki;

import javax.persistence.*;

@Entity
@Table(name = "RequestResults")
public class RequestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestID;
    @OneToOne
    private WikiRequest request;
    @Column(name = "ARTICLE_NAME")
    private String articleName;
    @Column(name = "ARTICLE_LINK")
    private String articleLink;

    public RequestResult() {
    }

    @JoinColumn(name = "ID")
    public int getRequestID() {
        return request.getId();
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setRequest(WikiRequest request) {
        this.request = request;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public void setArticleLink(String backReferenceLink) {
        this.articleLink = backReferenceLink;
    }
}
