package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.util.SearchFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SearchService is a service class.
 * it contains business logic to control the handling of a search query from a call to the /search endpoint.
 * it contains a single methods to format the query and query the database.
 */
@Service
public class SearchService {
    private final MountainPeakRepository peakRepo;
    private final SearchFormatter formatter;

    @Autowired
    public SearchService(MountainPeakRepository peakRepo, SearchFormatter formatter) {
        this.peakRepo = peakRepo;
        this.formatter = formatter;
    }

    /**
     *
     * @param query The search query from the http request
     * @return returns a ResponseEntity object with database query result and a response code,
     * or an error message and response code if the database query does not have any results.
     */
    public ResponseEntity<String> searchForMountainPeak(String query) {
        Gson gson = new Gson();
        query = formatter.format(query);
        List<Map<String,Long>> searchResults = peakRepo.getTop10MountainPeakIdByName(query);
        if (!(searchResults == null) && !searchResults.isEmpty()) {
            HttpStatus okStatus = HttpStatus.OK;
            String responseBody = gson.toJson(searchResults);
            return new ResponseEntity<>(responseBody, okStatus);
        } else return new ResponseEntity<>("Sorry, Could not find any mountains by that name, " +
                "ensure the spelling is correct(example: mount blanc does not exist, it's name is mont blanc), or if there is more than one peak by that name, " +
                "try including the home state after the name of the mountain like " +
                "\"Mount Washington (New Hampshire)" , HttpStatus.NOT_FOUND);
    }
}
