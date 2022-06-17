package com.mountainweatherScraper.api;

import com.mountainweatherScraper.api.service.DataService;
import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MountainWeatherScraperApiApplication {
	final  static DataScraper scraper = new DataScraper();
	final static DataService ds = new DataService(scraper);


	public static void main(String[] args) {
		SpringApplication.run(MountainWeatherScraperApiApplication.class, args);
	}
}
