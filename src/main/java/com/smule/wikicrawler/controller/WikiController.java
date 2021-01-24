package com.smule.wikicrawler.controller;

import com.smule.wikicrawler.dao.ResultService;
import com.smule.wikicrawler.dao.RequestService;
import com.smule.wikicrawler.service.ArticleAnalyzerService;
import com.smule.wikicrawler.service.WikiDataParserService;
import com.smule.wikicrawler.model.Result;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;


@Controller
public class WikiController {
    private final WikiDataParserService wikiDataParserService;
    @Autowired
    private RestTemplate httpTemplate;
    @Autowired
    private RequestService requestService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private ArticleAnalyzerService articleAnalyzerService;

    public WikiController() {
        this.wikiDataParserService = new WikiDataParserService();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/start_search_by_keyword")
    public String startAnalysis(
            @RequestParam(name = "keyword") String keyword, Model model) throws SQLException {
        String escapedKeyword = StringEscapeUtils.escapeHtml4(keyword.replaceAll(" ", "_"));
        int possibleId = requestService.getRequestByKeyword(escapedKeyword);
        if (possibleId != 0) {
            return String.format("redirect:/view_all_results?reqID=%s&keyword=%s", possibleId, escapedKeyword.toLowerCase());
        }
        int id = requestService.submitNewRequest(escapedKeyword);
        final String defaultStatus = "Pending";
        new Thread(() -> articleAnalyzerService.submitNewRequest(id, escapedKeyword)
        ).start();

        model.addAttribute("keyword", keyword);
        model.addAttribute("status", defaultStatus);
        model.addAttribute("reqID", id);

        return "statusResponse";
    }

    @GetMapping(path = "/start_search_by_keyword")
    public String redirectToIndexPage() {
        return "redirect:/";
    }


    @PostMapping("/get_search_status")
    public String getStatus(
            @RequestBody String requestData, Model model) {
        final String bodyParsingPattern = "\\=(\\w+)";
        final int matcherGroupID = 1;
        final String completeRequestStatus = "Completed";
        final String failedRequestStatus = "Failed";
        List<String> requestBody = wikiDataParserService.ParseData(requestData, bodyParsingPattern, matcherGroupID);
        int reqID = Integer.parseInt(requestBody.get(0));
        String keyword = requestBody.get(1);
        keyword = StringEscapeUtils.escapeHtml4(keyword);
        String unescapedKeyword = keyword.replaceAll("_", " ");

        String status = requestService.getRequestStatus(reqID);

        model.addAttribute("reqID", reqID);
        model.addAttribute("keyword", unescapedKeyword);
        model.addAttribute("status", status);
        if (status.equals(failedRequestStatus)) {
            return "requestResultFragment :: statusFailed";
        } else if (status.equals(completeRequestStatus)) {
            model.addAttribute("status", status);
            return "requestResultFragment :: statusCompleted";
        } else {
            model.addAttribute("status", status);
            return "requestResultFragment :: statusProcessing";
        }
    }

    @GetMapping("/view_all_results")
    public String showAllResults(
            @RequestParam(name = "reqID") String reqID,
            @RequestParam(name = "keyword") String keyword,
            Model model) {
        final String completedStatus = "Completed";
        List<Result> results = resultService.getRequestResults(Integer.parseInt(reqID));
        for (Result article : results){
            String articleName = article.getArticleName();
            articleName = StringEscapeUtils.unescapeHtml4(articleName.replaceAll("_", " "));
            article.setArticleName(articleName);
        }
        model.addAttribute("reqID", reqID);
        model.addAttribute("status", completedStatus);
        model.addAttribute("keyword", keyword);
        model.addAttribute("articlesList", results);

        return "requestResults";
    }
}