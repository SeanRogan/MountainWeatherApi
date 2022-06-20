package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.entities.MountainPeak;
import com.mountainweatherScraper.api.entities.MountainRange;
import com.mountainweatherScraper.api.entities.SubRange;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.repository.MountainRangeRepository;
import com.mountainweatherScraper.api.repository.SubRangeRepository;
import com.mountainweatherScraper.api.webscraper.DataScraper;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * DataService is a service class.
 * it contains business logic to control web-scraping of pages,
 * populating the database with relevant information about pages on server start-up,
 * and collect weather data from those pages.
 */
@NoArgsConstructor
@Service
public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    @Autowired
    public DataService(DataScraper ds,
                       MountainPeakRepository peakRepo,
                       MountainRangeRepository rangeRepo,
                       SubRangeRepository subRangeRepo) {
        this.ds = ds;
        this.peakRepo = peakRepo;
        this.rangeRepo = rangeRepo;
        this.subRangeRepo = subRangeRepo;
    }

    final private String baseUrl = "https://www.mountain-forecast.com";
    DataScraper ds;
    MountainPeakRepository peakRepo;
    MountainRangeRepository rangeRepo;
    SubRangeRepository subRangeRepo;

    //initialize DB with all range and peak information
    @Bean
    //initialize DB with all range and peak information
    public void init() {
        logger.info("Collecting Data to populate DB with mountain ranges, sub ranges, and mountain peaks.");
        //get all mountain ranges, key = name of range, value = range URI
        HashMap<String, String> mountainRangeUriMap = getAllMajorMountainRangeUrls();
        //for each range in list, create new mountain range entity,
        // getAllSubRanges() and getAllPeaksInRange() should trigger
        // creation of all peaks and sub range entities.
        mountainRangeUriMap.forEach((key, value) ->{
            logger.trace("Creating Mountain Range Entity in Database: "+ key);
            rangeRepo.save(new MountainRange(key,
                    baseUrl + value,
                    getAllSubRanges(value),
                    getAllPeaksInRange(value)));
        });
    }

    private Set<MountainPeak> getAllPeaksInRange(String uri) {

        String url = baseUrl + uri;
        Set<MountainPeak> peaks = new HashSet<>();
        //scrape the home range page
        Document homeRange = ds.scrapeDocument(url);
        //scrape all the sub range link elements from the table at the bottom of the page
        Elements elements = homeRange
                .getElementsByClass("b-list-table__item-name--ranges")
                .select("a[href]");
        //create set of strings to hold sub range links
        Set<String> setOfSubRangeLinks = new HashSet<>();
        //iterate thru link elements from home range page
        for(Element e : elements) {
            //save each link string to the set
            setOfSubRangeLinks.add(baseUrl + e.attr("href"));
        }
        //create a set to store the scraped document of each subrange page
        Set<Document> subRangePages = new HashSet<>();
        //loop through the link set of sub-ranges
        for(String s : setOfSubRangeLinks) {
            //scrape the link
            Document currentDoc = ds.scrapeDocument(s);
            //save all peak link elements
            Elements peakLinkElements = currentDoc.getElementsByClass("b-list-table__item-name")
                    .select("a[href]");
            //loop thru each peak link element
            for(Element e : peakLinkElements) {
                //add new peak entity to set
                peaks.add(new MountainPeak(e.text(),baseUrl + e.attr("href"), s));
            }
        }
        return peaks;
    }

    public Set<SubRange> getAllSubRanges(String uri) {
        String url = baseUrl + uri;
        Set<SubRange> subRanges = new HashSet<>();
        Document homeRange = ds.scrapeDocument(url);
        Elements elements = homeRange
                .getElementsByClass("b-list-table__item-name--ranges")
                .select("a[href]");
        for(Element e : elements) {
            subRanges.add(new SubRange(e.text(), baseUrl + e.attr("href"), uri));
        }
        return subRanges;
    }

    public HashMap<String,String> getAllMajorMountainRangeUrls() {
        HashMap<String,String> rangeUrls = new HashMap<>();
        String uri = baseUrl + "/mountain_ranges";
        Document allRanges = ds.scrapeDocument(uri);
        Elements elements = allRanges
                .getElementsByClass("b-list-table__item-name--ranges")
                .select("a[href]");
        for(Element e : elements) {
            //saves each range key = name string , value = url string
            rangeUrls.put(e.text(),e.attr("href"));
            //e.remove();
        }
        return rangeUrls;
    }

    public HashMap<String,String> getAllMountainPeakUrls() {
        //listOfPeakUrls is a Hashmap of mountain peak names(key) and their corresponding uri (value)
        HashMap<String,String> listOfPeakUrls = new HashMap<>();
        //document map contains the pages a-z containing
        // all the links of all mountain peaks (over 1000ft) in the USA
        Map<Character, Document> documentMap = new HashMap<>();
        //iterating through the integer values 65-90 which will be cast to A-Z characters
        // via their ascii values. then using the characters to fill in the variable in the uri.
        for(int i = 65; i <= 90; i++ ) {
            //num converted to capital Letter char
            char c = (char)i;
            //uri of
            String uri = String.format(baseUrl + "/countries/United-States/locations/%c" , c);
            documentMap.put(c, ds.scrapeDocument(uri));
        }

        for (Document current : documentMap.values()) {
            Elements elements = current.getElementsByClass("b-list-table__item").select("a");
            for (Element currentElement : elements) {
                listOfPeakUrls.put(currentElement.text(),
                        currentElement
                                .select("a[href]")
                                .attr("href"));

            }
        }
        return listOfPeakUrls;
    }

    /**
     *
     *
     * @param uri the uri of the specific mountain peak with which the weather data is associated.
     *
     * @return List<String[]> dataList - returns a list of string arrays,
     * each array containing all values of one category,
     * catagories being the high temperature, low temperature,
     * and windchill temps, along with wind and weather conditions
     * and rainfall snowfall estimates
     */
    public List<List<String>> getWeatherData(String uri) {
        List<List<String>> dataList = new ArrayList<>();
        String weatherConditionsRow = "forecast__table-summary";
        String maxTempRow = "forecast__table-max-temperature";
        String minTempRow = "forecast__table-min-temperature";
        String windChillRow = "forecast__table-chill";
        String rainFallRow = "forecast__table-rain";
        String snowFallRow = "forecast__table-snow";
        String windRow = "forecast__table-wind";
        Document doc = ds.scrapeDocument(uri);

        if(doc != null) {

            //get high temps
            Elements maxTempElements = doc.getElementsByClass(maxTempRow)
                    .select("span.temp");
            dataList.add(collectToList(maxTempElements.iterator()));

            //get low temps
            Elements minTempElements = doc.getElementsByClass(minTempRow)
                    .select("span.temp");
            dataList.add(collectToList(minTempElements.iterator()));

            //get windchill temps
            Elements windChillElements = doc.getElementsByClass(windChillRow)
                    .select("span.temp");
            dataList.add(collectToList(windChillElements.iterator()));

            //get snowfall
            Elements snowFallElements = doc.getElementsByClass(snowFallRow)
                    .select("td.forecast__table-relative")
                    .select("span.snow");
            dataList.add(collectToList(snowFallElements.iterator()));
            //get rainfall
            Elements rainFallElements = doc.getElementsByClass(rainFallRow)
                    .select("td.forecast__table-relative")
                    .select("span.rain");
            dataList.add(collectToList(rainFallElements.iterator()));

            //get weather elements
            Elements weatherConditionElements = doc.getElementsByClass(weatherConditionsRow)
                    .select("td");
            dataList.add(collectToList(weatherConditionElements.iterator()));

            //get wind elements
            Elements windElements = doc.getElementsByClass(windRow)
                    .select("tr.forecast__table-wind");
            dataList.add(getWindConditions(windElements.select("td.iconcell").iterator()));
        }

        return dataList;
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
            result.add("Wind Conditions: "
                    + current.select("text.wind-icon__val").text()
                    + " "
                    + current.select("div.wind-icon__tooltip").text());
        }

        return result;
    }
}
