package com.smule.wikicrawler.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "Keyword", nullable = false)
    private String keyword;

    @Column(name = "Status", nullable = false)
    private Status status;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "request", fetch = FetchType.EAGER)
    private final List<Result> results = new ArrayList<>();

    public Request(){}

    public Request(String keyword, Status status){
        this.keyword = keyword;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addResult(Result result){
        results.add(result);
        result.setRequest(this);
    }

    public List<Result> getResults() {
        return results;
    }
}
