package com.mountainweatherScraper.api.webscraper;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 *
 */
@Component
@NoArgsConstructor
public class DataScraper {
    //todo we need to get selenium up and running and probably use it here.
    // id like the datascraper to fetch the page with selenium and then use jsoup to go thru the returned html
    private static final Logger logger = LoggerFactory.getLogger(DataScraper.class);
    /**
     * @return Document - returns an HTML document object, scraped from the uri argument.
     *
     * @param uri - a webpage uri to scrape data from.
     */
    public Document scrapeDocument(String uri) {
        try {
            return Jsoup.connect(uri).get();
        } catch (IOException e)
        {
            logger.error("An error has occured in MountainWeatherScraper.api.webscraper.DataScraper.scrapeDocument() : \n"+
                    e.getMessage());
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

}
