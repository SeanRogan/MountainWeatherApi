package com.mountainweatherScraper.api.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchFormatter {

    //formats search to uri compatible string
    // ex. search "mount washington" would return "q=mount+washington"
    public String format(final String query) {
        return "q=" +(query.trim()).replace(' ','+');
    }
}

