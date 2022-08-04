package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//todo this is more of an integration test, need to create a unit test
@ExtendWith(MockitoExtension.class)
class WeatherDataServiceTest {

    @Mock
    private DataScraper dataScraper;
    private WeatherDataService serviceUnderTest;
    @BeforeEach
    void setUp() {
        serviceUnderTest = new WeatherDataService(dataScraper);
    }
    private final String uriToTest = "https://www.mountain-forecast.com/peaks/Mount-Washington-2/forecasts/1917";

    @Test
    void getWeatherData() {
        List<List<String>> methodResult = serviceUnderTest.getWeatherData(uriToTest, "F");
        //list always has an empty list at the end for some reason,
        // remove it before testing size/content
        if (methodResult.size() == 9) {
            methodResult.remove(methodResult.size() - 1);
        }
        for (List<String> l : methodResult) {
            if (l.isEmpty()) {
                fail();
            }
            assertEquals(8, methodResult.size());
        }
    }

    @AfterEach
    void tearDown() {

    }

}