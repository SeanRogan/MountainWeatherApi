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
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * DatabaseInitializerService is a service class.
 * it contains business logic to control web-scraping of pages,
 * populating the database with relevant information about pages on calling the init() method.
 */
@NoArgsConstructor
@Service
public class DatabaseInitializerService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataService.class);

    @Autowired
    public DatabaseInitializerService(DataScraper ds,
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
     * then loops through the map to create a new entity in the Postgres database for each major range,
     * along with their associated sub-ranges, and all the mountain peaks associates with that mountain range.
     */

    private boolean initialized;

    public boolean isInitialized() {
        return initialized;
    }

    public void init() {

        logger.info("Collecting Data to populate DB with mountain ranges, sub ranges, and mountain peaks.");
        //get all mountain ranges, key = name of range, value = range URI
        HashMap<String, String> mountainRangeUriMap = getAllMajorMountainRangeUrls();
        //for each range in list, create new mountain range entity,
        // getAllSubRanges() and getAllPeaksInRange() should trigger
        // creation of all peaks and sub range entities.
        if(!isInitialized()) {
            mountainRangeUriMap.forEach((key, value) -> {
                logger.info("Creating Mountain Range Entity in Database: " + key);
                rangeRepo.save(new MountainRange(key,
                        baseUrl + value,
                        getAllSubRanges(value),
                        getAllPeaksInRange(value)));
            });
            logger.info("Data collection is complete!");
            initialized = true;
        }
    }
    /**
     * @param uri - the uri of the subrange the peaks belong to
     *
     * @return Set<MountainPeak> returns a Set of all the MountainPeak objects associated with that subrange
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
}
