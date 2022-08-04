package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.ForecastBuilderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(ForecastController.class)
@AutoConfigureMockMvc
class ForecastControllerTest {
    @MockBean
    private ForecastBuilderService forecastBuilderService;
    @Autowired
    private MockMvc mockMvc;

    ResponseEntity<String> testResponse = new ResponseEntity<>("{Test:Response}" , HttpStatus.OK);


    @Test
    void getDailyForecastByMountainId() throws Exception {
        when(forecastBuilderService
                .createWeatherReportResponse(1L , 1 , "F"))
                .thenReturn(testResponse);

        //then
        this.mockMvc.perform(get("/report/daily/1")
                .header("Temp-format", "F"))
                .andExpect(status().isOk());

    }

    @Test
    void getSixDayForecastByMountain() throws Exception {
        when(forecastBuilderService
                .createWeatherReportResponse(1L , 6 , "F"))
                .thenReturn(testResponse);

        //then
        this.mockMvc.perform(get("/report/extended/1")
                        .header("Temp-format", "F"))
                .andExpect(status().isOk());

    }
}