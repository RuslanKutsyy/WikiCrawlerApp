package com.smule.wikicrawler.database;

import com.smule.wikicrawler.wiki.WikiRequest;
import org.springframework.data.repository.CrudRepository;

public interface WikiRequestRepository extends CrudRepository<WikiRequest, Integer> {
}
