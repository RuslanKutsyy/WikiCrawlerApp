package com.smule.wikicrawler.dao;

import com.smule.wikicrawler.dto.RequestDto;
import com.smule.wikicrawler.model.Request;
import org.springframework.data.repository.CrudRepository;

public interface WikiRequestRepository extends CrudRepository<Request, Integer> {
}
