package com.mountainweatherScraper.api.webscraper;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DataScraperTest {
    @Autowired
    DataScraper scraperUnderTest =  new DataScraper();
    @Test
    void scrapeDocument() {
        Document test = scraperUnderTest.scrapeDocument("https://www.google.com");
        assertThat(test.title()).isEqualTo("Google");
        }
    }
