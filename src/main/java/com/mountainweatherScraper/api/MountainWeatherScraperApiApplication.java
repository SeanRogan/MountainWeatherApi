package com.mountainweatherScraper.api;

import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MountainWeatherScraperApiApplication {



	final  static DataScraper scraper = new DataScraper();
	public static void main(String[] args) {
		SpringApplication.run(MountainWeatherScraperApiApplication.class, args);
		Document doc = search();
		System.out.println(doc.body());

	}
	public static Document search() {
		System.setProperty("webdriver.chrome.driver","/Users/seanrogan/Desktop/chromedriver");
		ChromeDriver driver = new ChromeDriver();
		//connect to main page
		driver.get("www.mountain-forecast.com");
		//find searchbar
		WebElement searchbar = driver.findElement(By.id("location"));
		//enter search query
		searchbar.sendKeys("bear mountain");
		//send 'hit enter' signal
		searchbar.sendKeys(Keys.RETURN);
		//scrape the search results page
		return scraper.scrapeDocument(driver.getCurrentUrl());

	}
}
