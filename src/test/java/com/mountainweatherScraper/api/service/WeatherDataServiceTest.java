package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDataServiceTest {
    @Autowired
    WeatherDataService serviceUnderTest =  new WeatherDataService(new DataScraper());
    private String uriToTest = "https://www.mountain-forecast.com/peaks/Mount-Washington-2/forecasts/1917";

    @Test
    void getWeatherData() {
        List<List<String>> methodResult = serviceUnderTest.getWeatherData(uriToTest,"F");
        methodResult.remove(methodResult.size()-1);
        if(methodResult != null) {
            for(List<String> l : methodResult) {
                if(l.isEmpty()) {
                }
            }
            assertEquals(8 , methodResult.size());
        } else {
            fail();
        }
    }

}