package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.ForecastBuilderService;
import com.mountainweatherScraper.api.service.RequestHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
/**
 * ReportsController is a controller class. it handles requests for a weather forecast to be returned.
 */
@Controller
@RequestMapping("/report")
public class ReportsController {
//todo need a get mapping to return mountainid by mountain name and state
// todo future feature : request handler takes the request and handles authentication/authorization

    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
    @Autowired
    public void setForecastBuilderService(ForecastBuilderService service) {
        this.service = service;
    }
    ForecastBuilderService service;

    /**
     * @param request The HttpServlet Request recieved by the controller
     *
     * @param mountainId the Mountain Peak Id number
     *
     *
     *
     */
    @GetMapping(value = "/daily/{mountainId}")
    public ResponseEntity<String> getDailyReportByMountain(HttpServletRequest request, @PathVariable Long mountainId) {
        logger.info("creating one day weather forecast for " + mountainId);
        return service.createWeatherReportResponse(mountainId, 1, request);
    }

    /**
     *
     */
    @GetMapping("/extended/{mountainId}")
    public ResponseEntity<String> getSixDayForecastByMountain(HttpServletRequest request, @PathVariable Long mountainId) {
        logger.info("creating six day weather forecast for " + mountainId);
        return service.createWeatherReportResponse(mountainId, 6, request);
    }
}
