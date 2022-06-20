package com.mountainweatherScraper.api.model;

import lombok.Data;

@Data
public class SearchQuery {
    private final String query;
    public SearchQuery(String query){
        this.query = query;
    }

}
