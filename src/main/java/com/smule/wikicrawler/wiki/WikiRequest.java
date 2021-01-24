package com.smule.wikicrawler.wiki;

import javax.persistence.*;

@Entity
public class WikiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "KEYWORD")
    private String keyword;
    @Column(name = "STATUS")
    private String status;

    public WikiRequest() {
    }

    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
