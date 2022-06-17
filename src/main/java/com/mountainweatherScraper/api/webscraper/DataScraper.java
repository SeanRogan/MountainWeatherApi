package com.mountainweatherScraper.api.webscraper;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@NoArgsConstructor
public class DataScraper {

    public Document scrapeDocument(String uri) {
        try {
            return Jsoup.connect(uri).get();
        } catch (IOException e)
        {
            //todo fill catch block
        }
        return null;
    }

}
