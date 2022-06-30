package com.mountainweatherScraper.api.model;

import java.io.Serializable;

public class Report implements Serializable {
    public Report(
            String peakName,
            DayOfTheWeek DayOfTheWeek,
            String high,
            String low,
            String windChill,
            float expectedSnowfall,
            float expectedRainfall,
            String weatherConditions,
            String windConditions) {
        this.peakName = peakName;
        this.DayOfTheWeek = DayOfTheWeek;
        this.high = high;
        this.low = low;
        this.windChill = windChill;
        this.expectedSnowfall = expectedSnowfall;
        this.expectedRainfall = expectedRainfall;
        this.weatherConditions = weatherConditions;
        this.windConditions = windConditions;
    }
    private final DayOfTheWeek DayOfTheWeek;
    private final String peakName;
    private final String high;
    private final String low;
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

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
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
