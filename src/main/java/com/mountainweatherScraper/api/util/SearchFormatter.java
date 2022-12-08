package com.mountainweatherScraper.api.util;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The SearchFormatter class formats the query string from url encoded
 * to a string that will match the keywords in the database
 */
@NoArgsConstructor
@Component
public class SearchFormatter {
    Logger logger = LoggerFactory.getLogger(SearchFormatter.class);
    //formats search string received from request from uri format to a normal string.
    // ex. a search for "mount washington" would come in as "query=mount+washington"
    /**
     * formats the query string by changing to lowercase and replacing '+' characters with whitespace.
     * @param query the query as a String to be formatted
     * @return the formatted query as a String
     */
    public String format(final String query) {
        logger.info("Formatting Search Query for use in database Query");
        return query.toLowerCase().trim().replace("+"," ");
    }
}

