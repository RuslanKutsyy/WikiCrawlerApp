package com.smule.wikicrawler.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

    public List<String> parseData(String data, String paternString, int matcherGroupID) {
        List<String> foundData = new ArrayList<>();
        Pattern patern = Pattern.compile(paternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patern.matcher(data);

        while (matcher.find()) {
            foundData.add(matcher.group(matcherGroupID));
        }

        return foundData;
    }
}
