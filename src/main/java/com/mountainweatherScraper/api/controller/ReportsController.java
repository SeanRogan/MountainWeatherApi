package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.model.Report;
import com.mountainweatherScraper.api.service.ForecastBuilderService;
import com.mountainweatherScraper.api.service.ReportBuilderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportsController {
//todo this is all wrong fix this
    @GetMapping("/daily/{mountainId}&{rangeId}")
    public String getDailyReportByMountain(@PathVariable String mountainId, @PathVariable String rangeId) {
        ReportBuilderService service = new ReportBuilderService(mountainId,rangeId);
        return "ok this isnt working yet";
    }
    @GetMapping("/extended/{mountainId}")
    public void getSixDayForecastByMountain(@PathVariable String mountainId) {
        ForecastBuilderService service = new ForecastBuilderService(mountainId);
    }

}
