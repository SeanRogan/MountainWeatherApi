package com.mountainweatherScraper.api.model;

import com.mountainweatherScraper.api.entities.Report;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Forecast implements Serializable {
    private final Map<String, Report> forecastData;
    public Map<String, Report> getForecastData() {
        return forecastData;
    }
    public Forecast(Report amReport, Report pmReport, Report nightReport) {
        Map<String,Report> forecastDataMap = new HashMap<>();

        forecastDataMap.put("AM",amReport);
        forecastDataMap.put("PM",pmReport);
        forecastDataMap.put("Night",nightReport);
        this.forecastData = forecastDataMap;
    }

}
