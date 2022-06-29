package com.mountainweatherScraper.api.model;

import java.io.Serializable;

public class Report implements Serializable {
    public Report(
            String peakName,
            String maxTemp,
            String minTemp,
            String windChill,
            float expectedSnowfall,
            float expectedRainfall,
            String weatherConditions,
            String windConditions) {
        this.peakName = peakName;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.windChill = windChill;
        this.expectedSnowfall = expectedSnowfall;
        this.expectedRainfall = expectedRainfall;
        this.weatherConditions = weatherConditions;
        this.windConditions = windConditions;
    }

    private final String peakName;
    private final String maxTemp;
    private final String minTemp;
    private final String windChill;
    private final float expectedRainfall;
    private final float expectedSnowfall;
    private final String weatherConditions;
    private final String windConditions;


    public String getPeakName() {
        return peakName;
    }
    public String getWindConditions() {
        return windConditions;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getWindChill() {
        return windChill;
    }

    public float getExpectedRainfall() {
        return expectedRainfall;
    }

    public float getExpectedSnowfall() {
        return expectedSnowfall;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }
}
