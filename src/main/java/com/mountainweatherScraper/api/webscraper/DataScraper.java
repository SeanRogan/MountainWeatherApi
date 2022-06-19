package com.mountainweatherScraper.api.webscraper;

import com.mountainweatherScraper.api.service.DataService;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@NoArgsConstructor
public class DataScraper {
    private static final Logger logger = LoggerFactory.getLogger(DataScraper.class);
    public Document scrapeDocument(String uri) {
        try {
            return Jsoup.connect(uri).get();
        } catch (IOException e)
        {
            logger.error("An error has occured in MountainWeatherScraper.api.webscraper.DataScraper.scrapeDocument() : "+ e.getMessage());
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

}
