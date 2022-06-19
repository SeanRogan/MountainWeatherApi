package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.ForecastBuilderService;
import com.mountainweatherScraper.api.service.RequestHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/report")
public class ReportsController {
//todo need a get mapping to return mountainid by mountain name and state

// todo future feature : request handler takes the request and handles authentication/authorization

    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
    @GetMapping(value = "/daily/{mountainId}")
    public ResponseEntity<String> getDailyReportByMountain(HttpServletRequest request, @PathVariable Long mountainId) {
        RequestHandlerService reqHandler = new RequestHandlerService(request);
        //if request is correct, instantiate the report builder service
        ForecastBuilderService service = new ForecastBuilderService();
        return service.createWeatherReportResponse(mountainId, 1);

        //if request is malformed, go to error handler?
    }
    @GetMapping("/extended/{mountainId}")
    public ResponseEntity<String> getSixDayForecastByMountain(@PathVariable Long mountainId) {
        ForecastBuilderService service = new ForecastBuilderService();
        return service.createWeatherReportResponse(mountainId, 6);
    }

}
