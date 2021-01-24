package com.smule.wikicrawler.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WikiDataParserService {

    public List<String> ParseData(String data, String patternString, int matcherGroupID) {
        List<String> foundData = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            foundData.add(matcher.group(matcherGroupID));
        }

        return foundData;
    }
}
