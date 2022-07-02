package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.entities.MountainPeak;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.service.ForecastBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.List;

@Controller
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
    ForecastBuilderService service;
    ReportsController reportsController;
    MountainPeakRepository peakRepo;

    @Autowired
    public DemoController(ForecastBuilderService service, MountainPeakRepository peakRepo, ReportsController reportsController) {
        this.service = service;
        this.peakRepo = peakRepo;
        this.reportsController = reportsController;
    }

    @GetMapping("/demo")
    ModelAndView DemoViewListAllPeaks() {
        ModelAndView mav = new ModelAndView("demo-view");
        List<MountainPeak> listOfPeaks = peakRepo.getAllPeakNames();
        mav.addObject("peak_list" , listOfPeaks);
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
