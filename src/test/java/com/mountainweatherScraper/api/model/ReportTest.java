package com.mountainweatherScraper.api.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ReportTest {

    Report report = new Report.ReportBuilder()
                .day("Monday")
                .name("Test Peak")
                .high("75")
                .low("50")
                .chill("40")
                .rain(2.2f)
                .snow(0.0f)
                .wind("NE20")
                .weatherConditions("Sunny")
                .build();

    @Test
    void assertReportConstructionWorks() {
        Assertions.assertEquals("Monday",report.getDayOfTheWeek());
        Assertions.assertEquals("Test Peak",report.getPeakName());
        Assertions.assertEquals("75",report.getHigh());
        Assertions.assertEquals("50",report.getLow());
        Assertions.assertEquals("40",report.getWindChill());
        Assertions.assertEquals(2.2f,report.getExpectedRainfall());
        Assertions.assertEquals(0.0f,report.getExpectedSnowfall());
        Assertions.assertEquals("NE20",report.getWindConditions());
        Assertions.assertEquals("Sunny",report.getWeatherConditions());
    }

}