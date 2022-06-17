package com.mountainweatherScraper.api.model;

import com.mountainweatherScraper.api.entities.Report;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Forecast implements Serializable {
    private Map<String, Report> forecastData;

    public Map<String, Report> getForecastData() {
        return forecastData;
    }
    public Forecast(Report amReport, Report pmReport, Report nightReport) {
        Map<String,Report> forecastDataMap = new HashMap<>();
        forecastData.put("AM",amReport);
        forecastData.put("PM",pmReport);
        forecastData.put("Night",nightReport);
        this.forecastData = forecastDataMap;
    }

}
