package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * The Search Controller is a Controller class. It controls the search functionality of the API.
 *
 */
@Controller
public class SearchController {

    private final SearchService service;

    @Autowired
    public SearchController(SearchService service) {
        this.service = service;
    }

    /**
     *
     * @param query the search query passed in the request.
     * @return returns a Response Entity with the response of the database query and a 200 OK code,
     * or an error code and error message if the query is unsuccessful.
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchForPeakByName(@RequestParam String query) {
        return service.searchForMountainPeak(query);
    }
}
