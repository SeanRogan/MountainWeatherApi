package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.webscraper.DataScraper;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * WeatherDataService is a service class.
 * it contains business logic to control web-scraping of pages,
 * populating the database with relevant information about pages on server start-up,
 * and collect weather data from those pages.
 */
@NoArgsConstructor
@Service
public class WeatherDataService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataService.class);
    private DataScraper ds;

    @Autowired
    public WeatherDataService(DataScraper ds) {
        this.ds = ds;
    }


    /**
     * @param uri the uri of the specific mountain peak with which the weather data is associated.
     * @param tempFormat the temperature units the weather report is to be displayed in; Fahrenheit or Celsius
     * @return List<String[]> dataList - returns a list of string arrays,
     * each array containing all the values of one of 7 categories,
     * categories being the high temperatures, low temperatures,
     * and windchill temps, along with wind conditions, weather summary,
     * and rainfall / snowfall estimates.
     *
     *
     * The getWeatherData method collects the weather data needed for a Report object response.
     *  if the request was sent with http headers Temp-format : F,
     *  the temperature values will be converted to imperial units
     *  via the National Institute of Standards and Technology formula : °F = (°C × 1.8) + 32
     */
    public List<List<String>> getWeatherData(String uri , String tempFormat) {

        List<List<String>> dataList = new ArrayList<>(7);
        //html tags to pull information from the webpage
        String weatherConditionsRow = "forecast__table-summary";
        String maxTempRow = "forecast__table-max-temperature";
        String minTempRow = "forecast__table-min-temperature";
        //windchill is used on the site to be scraped html tags during colder months of the year...
        String windChillRow = "forecast__table-chill";
        //...feels is used during warmer months
        String windChillAlt = "forecast__table-feels";
        String rainFallRow = "forecast__table-rain";
        String snowFallRow = "forecast__table-snow";
        String windRow = "forecast__table-wind";
        String dayAndDate = "forecast__table-days-content";
        logger.info("scraping weather data from: " + uri);
        //attempt to scrape webpage
        Document doc = ds.scrapeDocument(uri);
        //if the document comes back from the datascraper, begin to pull info from it and collect it in a List
        if(doc != null) {

            //get high temps
            Elements maxTempElements = doc.getElementsByClass(maxTempRow)
                    .select("span.temp");
            dataList.add(0,collectToList(maxTempElements.iterator()));

            //get low temps
            Elements minTempElements = doc.getElementsByClass(minTempRow)
                    .select("span.temp");
            dataList.add(1,collectToList(minTempElements.iterator()));

            //get windchill temps
            Elements windChillElements = doc.getElementsByClass(windChillRow)
                    .select("span.temp");
            dataList.add(2,collectToList(windChillElements.iterator()));
            //during warm weather the data source changes 'wind chill' to 'feels like',
            // so check if windchillelements failed to collect anything
            // and use the 'feels' tag to scrape the wind chill data if thats the case
            if(dataList.get(2).isEmpty()) {
                windChillElements = doc.getElementsByClass(windChillAlt)
                        .select("span.temp");
                dataList.add(2,collectToList(windChillElements.iterator()));
            }
                try{
                    if(tempFormat.equals("F")){
                        logger.info("converting temperature values to Imperial Units");
                        List<String> convertedMax =  convertTempsToImperial(dataList.get(0));
                        dataList.remove(0);
                        dataList.add(0, convertedMax);
                        List<String> convertedMin =  convertTempsToImperial(dataList.get(1));
                        dataList.remove(1);
                        dataList.add(1, convertedMin);
                        List<String> convertedWindChill =  convertTempsToImperial(dataList.get(2));
                        dataList.remove(2);
                        dataList.add(2, convertedWindChill);
                    }
                } catch (NullPointerException e) {
                    logger.warn(e.getMessage() + ": \n A NullPointer Exception was thrown because there was no valid Temp-format header value provided");
                }
            //get snowfall
            Elements snowFallElements = doc.getElementsByClass(snowFallRow)
                    .select("td.forecast__table-relative")
                    .select("span.snow");
            dataList.add(3, collectToList(snowFallElements.iterator()));
            //get rainfall
            Elements rainFallElements = doc.getElementsByClass(rainFallRow)
                    .select("td.forecast__table-relative")
                    .select("span.rain");
            dataList.add(4, collectToList(rainFallElements.iterator()));

            //get weather elements
            Elements weatherConditionElements = doc.getElementsByClass(weatherConditionsRow)
                    .select("td");
            dataList.add(5, collectToList(weatherConditionElements.iterator()));

            //get wind elements
            Elements windElements = doc.getElementsByClass(windRow)
                    .select("tr.forecast__table-wind");
            dataList.add(6, getWindConditions(windElements.select("td.iconcell").iterator()));

            Elements dateElements = doc.getElementsByClass(dayAndDate).select("div > div:eq(1)");
            Elements dayOfWeekElements = doc.getElementsByClass(dayAndDate).select("div > div:eq(0)");
            dataList.add(7, getDayAndDateElements(dateElements, dayOfWeekElements));
            //get days of the week
        }
        return dataList;
    }
    private List<String> getDayAndDateElements(Elements dateElements, Elements dayOfWeekElements) {
        List<String> daysOfTheWeek = new ArrayList<>();
        List<String> daysOfTheMonth = new ArrayList<>();
        //todo this could be multithreaded to improve performance maybe?
        dateElements.forEach(i -> daysOfTheMonth.add(i.text()));
        dayOfWeekElements.forEach(i -> daysOfTheWeek.add(i.text()));
        List<String> results = new ArrayList<>(6);
        for(int i = 0; i<6; i++) {
            results.add(String.format("%s (%s)",daysOfTheWeek.get(i),daysOfTheMonth.get(i)));
        }
        return results;
    }
    private List<String> convertTempsToImperial(List<String> temps) {
        List<String> convertedTemps = new ArrayList<>();
        try {
            temps.forEach(i -> {
                double n = Double.parseDouble(i);
                n = n * 1.8 + 32;
                convertedTemps.add(String.valueOf(Math.round(n)));
            });
        } catch (NullPointerException e) {
            logger.warn("Null Pointer Exception thrown @ WeatherDataService.convertTempToImperial - " + e.getMessage());

        }
        return convertedTemps;
    }

    private List<String> collectToList(Iterator<Element> itr) {
        List<String> result = new ArrayList<>();

        while(itr.hasNext()) {
            Element current = itr.next();
            result.add(current.text());
        }

        return result;
    }

    private List<String> getWindConditions(Iterator<Element> itr) {

        List<String> result = new ArrayList<>();
        while(itr.hasNext()) {
            Element current = itr.next();
            result.add(current.select("text.wind-icon__val").text()
                    + " "
                    + current.select("div.wind-icon__tooltip").text());
        }

        return result;
    }
}
