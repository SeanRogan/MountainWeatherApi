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
 * DataService is a service class.
 * it contains business logic to control web-scraping of pages,
 * populating the database with relevant information about pages on server start-up,
 * and collect weather data from those pages.
 */
@NoArgsConstructor
@Service
public class WeatherDataService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataService.class);

    @Autowired
    public WeatherDataService(DataScraper ds) {
        this.ds = ds;
    }

    private DataScraper ds;

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
        String weatherConditionsRow = "forecast__table-summary";
        String maxTempRow = "forecast__table-max-temperature";
        String minTempRow = "forecast__table-min-temperature";
        String windChillRow = "forecast__table-chill";
        String windChillAlt = "forecast__table-feels";
        String rainFallRow = "forecast__table-rain";
        String snowFallRow = "forecast__table-snow";
        String windRow = "forecast__table-wind";
        String dayAndDate = "forecast__table-days-content";
        logger.info("scraping weather data from: " + uri);
        Document doc = ds.scrapeDocument(uri);

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

    /**
     *
     * @param dateElements
     * @param dayOfWeekElements
     * @return
     */
    private List<String> getDayAndDateElements(Elements dateElements, Elements dayOfWeekElements) {
        List<String> daysOfTheWeek = new ArrayList<>();
        List<String> daysOfTheMonth = new ArrayList<>();
        dateElements.forEach(i -> daysOfTheMonth.add(i.text()));
        dayOfWeekElements.forEach(i -> daysOfTheWeek.add(i.text()));
        List<String> results = new ArrayList<>(6);
        for(int i = 0; i<6; i++) {
            results.add(String.format("%s (%s)",daysOfTheWeek.get(i),daysOfTheMonth.get(i)));
        }
        return results;
    }

    /**
     *
     * @param temps
     * @return
     */
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

    /**
     *
     * @param itr
     * @return
     */
    private List<String> collectToList(Iterator<Element> itr) {
        List<String> result = new ArrayList<>();

        while(itr.hasNext()) {
            Element current = itr.next();
            result.add(current.text());
        }

        return result;
    }

    /**
     * @param itr an iterator of the wind condition html elements scraped from the web
     *
     *            the getWindConditions exists to concat the wind data. scraped data returns a wind speed
     *            and direction in the form of a compass bearing. this method combines the two into a single statistic
     * @return List<String> results - the wind conditions formatted as '{DIRECTION} {SPEED}' as a list of strings.
     */
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
