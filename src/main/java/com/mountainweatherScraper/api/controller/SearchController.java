package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final SearchService service;
    @Autowired
    public SearchController(SearchService service) {
        this.service = service;
    }
    @PostMapping("/search")
    public ResponseEntity<String> search(@RequestParam String query) {
        return service.search(query);
    }
}
