package com.smule.wikicrawler.dao;

import com.smule.wikicrawler.model.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WikiArticleResultRepository extends CrudRepository<Result, Integer> {
}
