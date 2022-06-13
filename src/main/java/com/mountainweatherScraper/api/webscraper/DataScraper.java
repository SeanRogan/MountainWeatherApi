package com.mountainweatherScraper.api.webscraper;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

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
