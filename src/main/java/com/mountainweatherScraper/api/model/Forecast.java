package com.mountainweatherScraper.api.model;

import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Data
public class Forecast {
    private Map<String,Report> forecastData;
    private String mountainId;

    public Forecast(String mountainId) {
        this.mountainId = mountainId;
    }

}
