package com.smule.wikicrawler.http;

import com.smule.wikicrawler.wiki.WikiResponseDto;
import com.smule.wikicrawler.wiki.WikipediaArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class WikiWebService {
    @Autowired
    RestTemplate webService;
    private final String baseUrl = "https://en.wikipedia.org/w/api.php?";


    public WikipediaArticle getWikiArticleContent(String title){
        WikiResponseDto article = null;
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
