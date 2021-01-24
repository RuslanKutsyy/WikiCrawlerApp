package com.smule.wikicrawler;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class WikiCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiCrawlerApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Executor getThreadPool(){
        Executor threadPool = Executors.newFixedThreadPool(20);
        return threadPool;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
