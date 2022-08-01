package com.mountainweatherScraper.api;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest

public class FrontEndTest {
    //need a test for the homepage, a test that all the links go to the right pages,
    // a test that clicking every button doesnt result in errors
    public void testButtonGoesToCorrectURL(String url , String buttonId, String expectedURL) {
        WebDriver driver = WebDriverManager.chromedriver().create();
        System.setProperty("webdriver.chrome.driver" , "./chromedriver");
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.id(buttonId)));
            try {
                driver.findElement(By.id(buttonId)).click();
            } catch (WebDriverException e) {
                e.printStackTrace();            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        Assert.assertEquals(expectedURL,driver.getCurrentUrl());
        driver.quit();
    }
    @Test
    public void testHomePageDemoLink() {
        testButtonGoesToCorrectURL("http://localhost:8080","home-demo-button","http://localhost:8080/demo");
    }
    @Test
    public void testHomePageDocumentationLink() {
        testButtonGoesToCorrectURL("http://localhost:8080","home-doc-button","http://localhost:8080/documentation");
    }
    @Test
    public void testHomePageRapidApiLink() {
        testButtonGoesToCorrectURL("http://localhost:8080","home-rapid-api-button","https://rapidapi.com/SeanRogan/api/peak-conditions");
    }

    @Test
    public void testDocPageRapidApiLink() {
        testButtonGoesToCorrectURL("http://localhost:8080/documentation","doc-rapid-api-button","https://rapidapi.com/SeanRogan/api/peak-conditions");
    }
    @Test
    public void testDocsPageDemoLink() {
        testButtonGoesToCorrectURL("http://localhost:8080/documentation","doc-demo-button","http://localhost:8080/demo");
    }
    @Test
    public void testDocsPageHomeLink() {
        testButtonGoesToCorrectURL("http://localhost:8080/documentation","doc-home-button","http://localhost:8080/");
    }
    @Test
    public void testDemoPageHomeLink() {
        testButtonGoesToCorrectURL("http://localhost:8080","demo-home-button","http://localhost:8080/");
    }
    @Test
    public void testDemoPageDocsLink() {
        testButtonGoesToCorrectURL("http://localhost:8080/demo","demo-doc-button","http://localhost:8080/documentation");
    }
    @Test
    public void testDemoPageRapidApiLink() {
        testButtonGoesToCorrectURL("http://localhost:8080/demo","home-rapid-api-button","https://rapidapi.com/SeanRogan/api/peak-conditions");
    }
    @Test
    public void testDemoPage() {

        System.setProperty("webdriver.chrome.driver" , "./chromedriver");
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/demo");

        for(WebElement e : driver.findElements(By.className("demo-btn"))) {
            e.click();
            String pageContents = driver.findElement(By.tagName("body")).getText();
            if(!pageContents.contains("forecastData")) {
                Assert.fail();
            }
        }
        driver.quit();
    }


}
