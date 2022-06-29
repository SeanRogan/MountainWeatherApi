package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.entities.SubRange;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.repository.MountainRangeRepository;
import com.mountainweatherScraper.api.repository.SubRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private MountainPeakRepository peakRepo;
    private MountainRangeRepository rangeRepo;
    private SubRangeRepository subrangeRepo;
    @Autowired
    public SearchService(MountainPeakRepository peakRepo, MountainRangeRepository rangeRepo, SubRangeRepository subrangeRepo) {
        this.peakRepo = peakRepo;
        this.rangeRepo = rangeRepo;
        this.subrangeRepo = subrangeRepo;
    }

    public ResponseEntity<String> search(String query) {
        ResponseEntity<String> response = null;
        //todo build all this..
        return response;
    }
}
