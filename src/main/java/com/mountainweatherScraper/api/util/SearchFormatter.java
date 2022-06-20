package com.mountainweatherScraper.api.util;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor
public class SearchFormatter {
    Logger logger = LoggerFactory.getLogger(SearchFormatter.class);
    //formats search to uri compatible string
    // ex. search "mount washington" would return "q=mount+washington"
    public String format(final String query) {
        logger.trace("Formatting Search Query for use in URLs");
        return "q=" +(query.toLowerCase().trim()).replace(' ','+');
    }
}

