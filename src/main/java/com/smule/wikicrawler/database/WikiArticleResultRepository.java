package com.smule.wikicrawler.database;

import com.smule.wikicrawler.wiki.RequestResult;
import org.springframework.data.repository.CrudRepository;

public interface WikiArticleResultRepository extends CrudRepository<RequestResult, Integer> {
}
