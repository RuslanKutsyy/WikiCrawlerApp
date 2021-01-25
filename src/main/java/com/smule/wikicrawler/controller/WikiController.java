package com.smule.wikicrawler.controller;

import com.smule.wikicrawler.dao.ResultService;
import com.smule.wikicrawler.dao.RequestService;
import com.smule.wikicrawler.dto.RequestDto;
import com.smule.wikicrawler.dto.ResultDto;
import com.smule.wikicrawler.model.Request;
import com.smule.wikicrawler.model.Status;
import com.smule.wikicrawler.service.ArticleAnalyzerService;
import com.smule.wikicrawler.service.WikiDataParserService;
import com.smule.wikicrawler.model.Result;
import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/submissions")
public class WikiController {
    private WikiDataParserService wikiDataParserService;
    private RestTemplate webService;
    private RequestService requestService;
    private ResultService resultService;
    private ArticleAnalyzerService articleAnalyzerService;
    private ModelMapper mapper;

    public WikiController(WikiDataParserService wikiDataParserService, RestTemplate webService, RequestService requestService, ResultService resultService, ArticleAnalyzerService articleAnalyzerService, ModelMapper mapper) {
        this.wikiDataParserService = wikiDataParserService;
        this.webService = webService;
        this.requestService = requestService;
        this.resultService = resultService;
        this.articleAnalyzerService = articleAnalyzerService;
        this.mapper = mapper;
    }

    @GetMapping()
    public String getSubmissions(){
        return "index";
    }

    @PostMapping()
    public ModelAndView submitNewRequest(
            @RequestParam(name = "keyword") String keyword) throws SQLException {
        String escapedKeyword = StringEscapeUtils.escapeHtml4(keyword.replaceAll(" ", "_"));
        int possibleId = requestService.getRequestByKeyword(escapedKeyword);
        if (possibleId != 0) {
            getSubmissionsById(possibleId, new ModelAndView());
        }
        int requestId = requestService.submitNewRequest(escapedKeyword);
        Request request = requestService.getRequestById(requestId);

        RequestDto requestDto = mapper.map(request, RequestDto.class);

        new Thread(() -> {
            articleAnalyzerService.submitNewRequest(requestDto.getId(), escapedKeyword);
        }).start();

        ModelAndView view = new ModelAndView("redirect:/submissions/" + requestId);
        view.addObject(requestDto);

        return view;
    }

    @GetMapping("/{id}")
    public ModelAndView getSubmissionsById(@PathVariable int id, ModelAndView view){
        Request request = requestService.getRequestById(id);

        if (request == null){
            return new ModelAndView("redirect:/submissions");
        }

        RequestDto requestDto = mapper.map(request, RequestDto.class);

        if (request.getStatus() != Status.Completed){
            view.setViewName("statusResponse");
            view.addObject(requestDto);
            return view;
        }
        view.setViewName("requestResults");
        view.addObject(requestDto);

        return view;
    }

    @GetMapping("/*")
    public ModelAndView pageDoesNotExist(){
        return new ModelAndView("redirect:/submissions");
    }
}