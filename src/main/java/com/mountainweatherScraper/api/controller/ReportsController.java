package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.ForecastBuilderService;
import com.mountainweatherScraper.api.service.ReportBuilderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportsController {

    @GetMapping("/daily/{mountainId}")
    public void getDailyReportByMountain(@PathVariable String mountainId) {
        ReportBuilderService reportBuilder = new ReportBuilderService(mountainId);
    }
    @GetMapping("/extended/{mountainId}")
    public void getSixDayForecastByMountain(@PathVariable String mountainId) {
        ForecastBuilderService service = new ForecastBuilderService(mountainId);
    }

}
