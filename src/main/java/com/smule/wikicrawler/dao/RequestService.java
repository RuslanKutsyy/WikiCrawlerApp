package com.smule.wikicrawler.dao;

import com.smule.wikicrawler.dto.RequestDto;
import com.smule.wikicrawler.model.Request;
import com.smule.wikicrawler.model.Status;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    private RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    public int submitNewRequest(String keyword) throws SQLException {
        int id = 0;
        Request request = new Request(keyword, Status.Pending);
        id = requestRepository.save(request).getId();

        return id;
    }

    public String getRequestStatus(int id) {
        return requestRepository.findById(id).get().getStatus().name();
    }

    public void updateRequestStatus(int id, Status status) {
        Request request = requestRepository.findById(id).get();
        if (request == null) {
            throw new IllegalArgumentException("There is no request with id " + id);
        }

        request.setStatus(status);
        requestRepository.save(request);
    }

    public int getRequestByKeyword(String keyword) {
        List<Request> data = new ArrayList<>();
        requestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));

        Request existingRequest = data.stream().filter(x -> x.getKeyword().equalsIgnoreCase(keyword)).findFirst().orElse(null);

        return existingRequest == null ? 0 : existingRequest.getId();
    }

    public Request getRequestById(int id) {
        List<Request> data = new ArrayList<>();
        requestRepository.findAll().forEach(wikiRequest -> data.add(wikiRequest));

        Request existingRequest = data.stream().filter(x -> x.getId() == id).findFirst().orElse(null);

        return existingRequest;
    }
}
