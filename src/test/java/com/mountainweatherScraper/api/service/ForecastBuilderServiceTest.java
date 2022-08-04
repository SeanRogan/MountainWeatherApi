package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ForecastBuilderServiceTest {
    @Mock
    private MountainPeakRepository mountainPeakRepository;
    @Mock
    private DataScraper scraper;


    @Mock
    private WeatherDataService weatherDataService;
    ForecastBuilderService serviceUnderTest;
    ResponseEntity<String> testResponse;
    List<List<String>> mockWeatherData = new ArrayList<>();

    @BeforeEach
    void setUp() {
        serviceUnderTest = new ForecastBuilderService(mountainPeakRepository,weatherDataService);
        testResponse = new ResponseEntity<>("test-Response",HttpStatus.OK );
        List<String> mockReport = new ArrayList<>(3);
        for(int i = 0; i < 9; i++) {
            mockWeatherData.add(mockReport);
        }
    }

    @Test
    void createWeatherReportResponse() {
        //todo this test breaks because the class requires
        // data scraping a website and its mock
        // is null so it throws index exception
        when(weatherDataService.getWeatherData( null , "F")).thenReturn(mockWeatherData);
        when(serviceUnderTest.createWeatherReportResponse(2L,1,"F")).thenReturn(testResponse);
        ResponseEntity<String> testResult = serviceUnderTest.createWeatherReportResponse( 2L,1,"F");
        assertEquals(testResult.getStatusCode(), HttpStatus.OK);

    }

    @AfterEach
    void tearDown() throws Exception{

    }

}