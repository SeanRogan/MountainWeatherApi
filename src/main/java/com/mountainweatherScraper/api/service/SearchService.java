package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.util.SearchFormatter;
import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.swing.text.Element;

public class SearchService {
    final private SearchFormatter formatter = new SearchFormatter();
    final private WebDriver driver = new ChromeDriver();
    final private DataScraper scraper = new DataScraper();
    public Document search(String query) {
        //connect to main page
        driver.get("www.mountain-forecast.com");
        //find searchbar
        WebElement searchbar = driver.findElement(By.id("location"));
        //enter search query
        searchbar.sendKeys(query);
        //send 'hit enter' signal
        searchbar.sendKeys(Keys.RETURN);
        //scrape the search results page
        return scraper.scrapeDocument(driver.getCurrentUrl());

    }
}
