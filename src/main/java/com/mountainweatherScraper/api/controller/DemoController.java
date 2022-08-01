package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.entities.MountainPeak;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.service.ForecastBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(ForecastController.class);
    ForecastBuilderService service;
    ForecastController forecastController;
    MountainPeakRepository peakRepo;

    @Autowired
    public DemoController(ForecastBuilderService service, MountainPeakRepository peakRepo, ForecastController forecastController) {
        this.service = service;
        this.peakRepo = peakRepo;
        this.forecastController = forecastController;
    }

    @GetMapping("/demo")
    ModelAndView DemoViewListAllPeaks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "100 ") Integer pageSize,
            @RequestParam(defaultValue = "peakName") String sortBy) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<MountainPeak> pagedResult = peakRepo.findAllDemoPeaks(pageable);
        List<MountainPeak> listOfPeaks;
        if(pagedResult.hasContent()) {
            listOfPeaks = pagedResult.getContent();
        }
        else listOfPeaks = peakRepo.getAllPeakNames();
        ModelAndView mav = new ModelAndView();
        mav.addObject("peak_list", listOfPeaks);
        return mav;
    }
    @GetMapping("/getExtendedForecastDemo")
    ResponseEntity<String> getExtendedForecastDemo(@RequestParam Long peakId) {
        return service.createWeatherReportResponse(peakId,6,"F");
    }
    @GetMapping("/getOneDayForecastDemo")
    ResponseEntity<String> getOneDayForecastDemo(@RequestParam Long peakId) {
        return service.createWeatherReportResponse(peakId,1,"F");
    }

}
