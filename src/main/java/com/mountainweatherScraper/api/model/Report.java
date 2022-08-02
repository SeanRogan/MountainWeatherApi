package com.mountainweatherScraper.api.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class Report implements Serializable {
    public Report(
            String peakName,
            String dayOfTheWeek,
            String high,
            String low,
            String windChill,
            float expectedSnowfall,
            float expectedRainfall,
            String weatherConditions,
            String windConditions) {
        this.peakName = peakName;
        this.dayOfTheWeek = dayOfTheWeek;
        this.high = high;
        this.low = low;
        this.windChill = windChill;
        this.expectedSnowfall = expectedSnowfall;
        this.expectedRainfall = expectedRainfall;
        this.weatherConditions = weatherConditions;
        this.windConditions = windConditions;
    }

    private final String dayOfTheWeek;
    private final String peakName;
    private final String high;
    private final String low;
    private final String windChill;
    private final float expectedRainfall;
    private final float expectedSnowfall;
    private final String weatherConditions;
    private final String windConditions;
}