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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ForecastBuilderServiceTest {
    @Mock
    private MountainPeakRepository mountainPeakRepository;
    @Mock
    private DataScraper scraper;
    @Spy
    @InjectMocks
    private WeatherDataService weatherDataService;
    private AutoCloseable autoCloseable;
    ForecastBuilderService serviceUnderTest;
    ResponseEntity<String> testResponse;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        serviceUnderTest = new ForecastBuilderService(mountainPeakRepository,weatherDataService);
        testResponse = new ResponseEntity<>("test-Response",HttpStatus.OK );

    }

    @Test
    void createWeatherReportResponse() {
        //todo this test breaks because the class requires
        // data scraping a website and its mock
        // is null so it throws index exception
        when(serviceUnderTest.createWeatherReportResponse(2L,1,"F")).thenReturn(testResponse);
        ResponseEntity<String> testResult = serviceUnderTest.createWeatherReportResponse( 2L,1,"F");
        assertEquals(testResult.getStatusCode(), HttpStatus.OK);

    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

}