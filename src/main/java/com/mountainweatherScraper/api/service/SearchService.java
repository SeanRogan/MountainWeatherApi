package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.repository.MountainRangeRepository;
import com.mountainweatherScraper.api.repository.SubRangeRepository;
import com.mountainweatherScraper.api.util.SearchFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    private MountainPeakRepository peakRepo;
    private final SearchFormatter formatter;

    @Autowired
    public SearchService(MountainPeakRepository peakRepo, SearchFormatter formatter) {
        this.peakRepo = peakRepo;
        this.formatter = formatter;
    }

    public ResponseEntity<String> searchForMountainPeak(String query) {
        Gson gson = new Gson();
        query = formatter.format(query);
        List<Map<String,Long>> searchResults = peakRepo.getTop10MountainPeakIdByName(query);
        if (!(searchResults == null)) {
            HttpStatus okStatus = HttpStatus.OK;
            String responseBody = gson.toJson(searchResults);
            return new ResponseEntity<>(responseBody, okStatus);
        } else return new ResponseEntity<>("Sorry, Could not find any mountains by that name, " +
                "ensure the spelling is correct, or if there is more than one peak by that name, " +
                "try including the home state after the name of the mountain like " +
                "\"Mount Washington (New Hampshire)" , HttpStatus.NOT_FOUND);
    }
}
