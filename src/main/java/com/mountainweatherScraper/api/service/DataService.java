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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
    /**
     * the init method initialized the Database : it connects to the website, collects all major mountain range name/uri info to a Map,
     * then loops through the map to create a new entity in the MySQL database for each major range,
     * along with their associated subranges, and all the mountain peaks associates with that mountain range.
     */
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
    /**
     * @param uri - the uri of the subrange
     */
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
    /**
     * @param uri the uri of the Major Range to be searched for sub-ranges.
     *
     * @return Set<Subrange> subRanges - returns a set of Subrange objects associates with the Major Range
     */
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
    /**
     * @return HashMap<String,String> listOfPeakUrls -  returns a Hashmap<String,String> of Mountain Range names with their associated URL
     */
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

    /**
     * @param uri the uri of the specific mountain peak with which the weather data is associated.
     *
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
        String rainFallRow = "forecast__table-rain";
        String snowFallRow = "forecast__table-snow";
        String windRow = "forecast__table-wind";
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
                } catch(NullPointerException e) {
                logger.error(e.getMessage(), "Null pointer exception thrown - required header 'Temp-format' not provided");
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
    }

        return dataList;
}

    private List<String> convertTempsToImperial(List<String> temps) {
        List<String> convertedTemps = new ArrayList<>();
        temps.forEach(i -> {
            double n = Double.parseDouble(i);
            n = n * 1.8 + 32;
            convertedTemps.add(Double.toString(n));
        });
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
            result.add("Wind Conditions: "
                    + current.select("text.wind-icon__val").text()
                    + " "
                    + current.select("div.wind-icon__tooltip").text());
        }

        return result;
    }
}
