package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.mountainweatherScraper.api.model.Report;
import com.mountainweatherScraper.api.model.Forecast;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Service
public class ForecastBuilderService{

    //todo need a way to handle fahrenheit/celsius conversion.
    // need to accept a header with a getForecast request specifying F or C,
    // default to C

    private static final Logger logger = LoggerFactory.getLogger(ForecastBuilderService.class);

    MountainPeakRepository peakRepo;
    DataService dataService;
    @Autowired
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
    @Autowired
    public void setPeakRepo(MountainPeakRepository peakRepo) {
        this.peakRepo = peakRepo;
    }

    public ForecastBuilderService() {
    }
    /**
     *
     */
    public ResponseEntity<String> createWeatherReportResponse(Long peakId, int numberOfDays) {
        Gson g = new Gson();
        var weatherForecast = buildListOfForecasts(peakId, dataService.getWeatherData(
                peakRepo.getPeakUriByPeakId(peakId)), numberOfDays);

        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();
        //set headers
        String body = g.toJson(weatherForecast);
        ResponseEntity<String> response;
        response = new ResponseEntity<>(body, headers, status);
        logger.info("created responseEntity : \n" + response +"\n");
        return response;

    }
    /**
     *
     */
    public Forecast buildForecast(Long peakId, List<List<String>> weatherData , int num) {
        //max temps, min, chill, snow, rain weather, wind
        List<String> maxTemps = weatherData.get(0);
        List<String> minTemps = weatherData.get(1);
        List<String> windChillTemps = weatherData.get(2);
        List<String> snowForecast = weatherData.get(3);
        List<String> rainForecast = weatherData.get(4);
        List<String> weatherSummary = weatherData.get(5);
        List<String> windCondition = weatherData.get(6);
        logger.trace("replacing: - with: 0.0 in precipitation forecasts");
        Collections.replaceAll(snowForecast, "-","0.0");
        Collections.replaceAll(rainForecast, "-","0.0");
        logger.trace("creating AM report for day" + num);
        Report amReport = new Report(peakId,
                maxTemps.get(num),
                minTemps.get(num),
                windChillTemps.get(num),
                Float.parseFloat(snowForecast.get(num)),
                Float.parseFloat(rainForecast.get(num)),
                weatherSummary.get(num),
                windCondition.get(num));
        logger.trace("creating PM report for day" + num);
        Report pmReport = new Report(peakId,
                maxTemps.get(num+1),
                minTemps.get(num+1),
                windChillTemps.get(num+1),
                Float.parseFloat(snowForecast.get(num+1)),
                Float.parseFloat(rainForecast.get(num+1)),
                weatherSummary.get(num+1),
                windCondition.get(num+1));
        logger.trace("creating NIGHT report for day" + num);
        Report nightReport = new Report(peakId,
                maxTemps.get(num+2),
                minTemps.get(num+2),
                windChillTemps.get(num+2),
                Float.parseFloat(snowForecast.get(num+2)),
                Float.parseFloat(rainForecast.get(num+2)),
                weatherSummary.get(num+2),
                windCondition.get(num+2));
        logger.trace("returning Forecast");
        return new Forecast(amReport,pmReport,nightReport);

    }
    /**
     *
     */
    List<Forecast> buildListOfForecasts(Long peakId, List<List<String>> weatherData, int day) {
        List<Forecast> forecastList = new ArrayList<>();

        for(int n = 0; n < day; n++) {
            forecastList.add(buildForecast(peakId,weatherData,n));
        }
        return forecastList;
    }


}
