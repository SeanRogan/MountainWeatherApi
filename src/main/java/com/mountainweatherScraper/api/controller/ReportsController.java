package com.mountainweatherScraper.api.controller;

import com.google.gson.JsonObject;
import com.mountainweatherScraper.api.entities.Report;
import com.mountainweatherScraper.api.service.ForecastBuilderService;
import com.mountainweatherScraper.api.service.ReportBuilderService;
import com.mountainweatherScraper.api.service.RequestHandlerService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/report")
public class ReportsController {
//todo need a get mapping to return mountainid by mountain name and state,
// need


    @GetMapping(value = "/daily/{rangeId}&{mountainId}")
    public ResponseEntity<String> getDailyReportByMountain(HttpServletRequest request, @PathVariable Long mountainId, @PathVariable Long rangeId) {
        //request handler takes the request and handles authentication/authorization
        new RequestHandlerService(request);

        //if request is correct, instantiate the report builder service
        ReportBuilderService service = new ReportBuilderService();
        return service.createReportResponse(mountainId, rangeId);

        //if request is malformed, go to error handler?
    }
    @GetMapping("/extended/{mountainId}")
    public String getSixDayForecastByMountain(@PathVariable String mountainId) {
        ReportBuilderService reportService = new ReportBuilderService();
        ForecastBuilderService service = new ForecastBuilderService(mountainId);

        return "not working yet";
    }

}
