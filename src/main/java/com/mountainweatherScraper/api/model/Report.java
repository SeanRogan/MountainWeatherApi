package com.mountainweatherScraper.api.model;

import lombok.Data;

@Data
public class Report {

    public class WeatherReport {

    }
        private Long weatherReportId;
        private Long peakId;
        private Long rangeId;
        private String maxTemp;
        private String minTemp;
        private String windChill;
        private String windSpeed;
        private float expectedRainfall;
        private float expectedSnowfall;
        private String windDirection;
        private String weatherConditions;
}
