package com.smule.wikicrawler.dao;

import com.smule.wikicrawler.dto.RequestDto;
import com.smule.wikicrawler.model.Request;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WikiRequestService{
    private WikiRequestRepository wikiRequestRepository;

    public WikiRequestService(WikiRequestRepository wikiRequestRepository){
        this.wikiRequestRepository = wikiRequestRepository;
    }

    public int submitNewRequest(String keyword) throws SQLException {
        final String status = "Pending";
        int id = 0;
        Request request = new Request(keyword, status);
        id = wikiRequestRepository.save(request).GetId();

        return id;
    }

    public String getRequestStatus(int id) {
        return wikiRequestRepository.findById(id).get().GetStatus();
    }

    public void changeRequestStatus(int id, String status) {
        List<Request> data = new ArrayList<>();
        wikiRequestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));
        Request existingRequest = data.stream().filter(x -> x.GetId() == id).findFirst().get();

        existingRequest.SetStatus(status);
        wikiRequestRepository.save(existingRequest);
    }

    public Integer getRequestIdByKeyword(String keyword) {
        List<Request> data = new ArrayList<>();
        wikiRequestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));

        Request existingRequest = data.stream().filter(x -> x.GetKeyword().equalsIgnoreCase(keyword)).findFirst().orElse(null);

        return existingRequest == null ? 0 : existingRequest.GetId();
    }

    public RequestDto getRequestById(int id) {
        List<Request> data = new ArrayList<>();
        wikiRequestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));
        wikiRequestRepository.findById(id).map(this -> new RequestDto());

        Request existingRequest = data.stream().filter(x -> x.GetId() == id).findFirst().orElse(null);

        return existingRequest;
    }
}
