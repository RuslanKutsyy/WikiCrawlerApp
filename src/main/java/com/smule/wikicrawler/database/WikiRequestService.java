package com.smule.wikicrawler.database;

import com.smule.wikicrawler.wiki.WikiRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WikiRequestService {
    @Autowired
    private WikiRequestRepository wikiRequestRepository;

    public int addNewRequestToDatabase(String keyword) throws SQLException {
        final String status = "Pending";
        WikiRequest request = new WikiRequest();
        request.setStatus(status);
        request.setKeyword(keyword);
        return wikiRequestRepository.save(request).getId();
    }

    public String getRequestStatus(int id) {
        return wikiRequestRepository.findById(id).get().getStatus();
    }

    public void changeRequestStatus(int id, String status) {
        List<WikiRequest> data = new ArrayList<>();
        wikiRequestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));
        WikiRequest existingRequest = data.stream().filter(x -> x.getId() == id).findFirst().get();

        existingRequest.setStatus(status);
        wikiRequestRepository.save(existingRequest);
    }

    public Integer getRequestIdByKeyword(String keyword) {
        List<WikiRequest> data = new ArrayList<>();
        wikiRequestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));

        WikiRequest existingRequest = data.stream().filter(x -> x.getKeyword().equalsIgnoreCase(keyword)).findFirst().orElse(null);

        return existingRequest == null ? 0 : existingRequest.getId();
    }

    public WikiRequest getRequestById(int id) {
        List<WikiRequest> data = new ArrayList<>();
        wikiRequestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));

        WikiRequest existingRequest = data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);

        return existingRequest;
    }
}
