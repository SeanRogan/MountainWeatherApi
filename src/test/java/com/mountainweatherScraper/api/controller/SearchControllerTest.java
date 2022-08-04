package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc
class SearchControllerTest {

    @MockBean
    SearchService searchService;

    @Autowired
    MockMvc mockMvc;
    private ResponseEntity<String> testResponse = new ResponseEntity<>("body" , HttpStatus.OK);
    @Test
    void searchForPeak() throws Exception {

        when(searchService.searchForMountainPeak("Test")).thenReturn(testResponse);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/search?query=Test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));
    }
}