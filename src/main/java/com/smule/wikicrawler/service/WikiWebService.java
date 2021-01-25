package com.smule.wikicrawler.service;

import com.smule.wikicrawler.dto.WikiResponseDto;
import com.smule.wikicrawler.dto.WikipediaArticleDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class WikiWebService {
    private RestTemplate webService;
    private final String baseUrl = "https://en.wikipedia.org/w/api.php?";

    public WikiWebService(RestTemplate restTemplate){
        this.webService = restTemplate;
    }


    public WikipediaArticleDto getWikiArticleContent(String title){
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("action", "parse");
        params.add("section", "0");
        params.add("prop", "text");
        params.add("format", "json");
        params.add("page", title);

        URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl).queryParams(params).build().encode().toUri();

        HttpEntity responseEntity = new HttpEntity<>(headers);
        ResponseEntity<WikiResponseDto> response = webService.exchange(targetUrl, HttpMethod.GET, responseEntity, WikiResponseDto.class);

        if (response.getBody().getParse().getTitle() == null){
            return null;
        }

        return response.getBody().getParse();
    }
}
