

package com.mountainweatherScraper.api.model;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class Report implements Serializable {

        public Report() {}

        public Report(
              Long peakId,
              String maxTemp,
              String minTemp,
              String windChill,
              float expectedSnowfall,
              float expectedRainfall,
              String weatherConditions,
              String windConditions) {
                      this.peakId = peakId;
                      this.maxTemp = maxTemp;
                      this.minTemp = minTemp;
                      this.windChill = windChill;
                      this.expectedSnowfall = expectedSnowfall;
                      this.expectedRainfall = expectedRainfall;
                      this.weatherConditions = weatherConditions;
                      this.windConditions = windConditions;
              }

        private Long weatherReportId;
        private Long peakId;
        private Long rangeId;
        private String maxTemp;
        private String minTemp;
        private String windChill;
        private float expectedRainfall;
        private float expectedSnowfall;
        private String weatherConditions;
        private String windConditions;

        public String getWindConditions() {
                return windConditions;
        }

        public Long getWeatherReportId() {
                return weatherReportId;
        }

        public Long getPeakId() {
                return peakId;
        }

        public Long getRangeId() {
                return rangeId;
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
