package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.mountainweatherScraper.api.entities.Report;
import com.mountainweatherScraper.api.model.Forecast;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ForecastBuilderService{

    //todo need a way to handle fahrenheit/celsius conversion

    private static final Logger logger = LoggerFactory.getLogger(ForecastBuilderService.class);
    Gson g = new Gson();
    MountainPeakRepository peakRepo;
    DataService dataService =  new DataService();
    public ForecastBuilderService() {
    }
    public ResponseEntity<String> createWeatherReportResponse(Long peakId, int numberOfDays) {
        var weatherForecast = buildListOfForecasts(peakId, dataService.getWeatherData(
                peakRepo.getPeakUriByPeakId(peakId)), numberOfDays);
        //todo need if statement to make sure weather forecast is valid
        // before building this response and
        // an error response for when it isnt valid
        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();
        //set headers
        String body = g.toJson(weatherForecast);
        ResponseEntity<String> response;
        response = new ResponseEntity<>(body, headers, status);

        return response;

    }

    public Forecast buildForecast(Long peakId, List<List<String>> weatherData , int num) {
        //max temps, min, chill, snow, rain weather, wind
        List<String> maxTemps = weatherData.get(0);
        List<String> minTemps = weatherData.get(1);
        List<String> windChillTemps = weatherData.get(2);
        List<String> snowForecast = weatherData.get(3);
        List<String> rainForecast = weatherData.get(4);
        List<String> weatherSummary = weatherData.get(5);
        List<String> windCondition = weatherData.get(6);

    Report amReport = new Report(peakId, maxTemps.get(num), minTemps.get(num), windChillTemps.get(num), Float.parseFloat(snowForecast.get(num)), Float.parseFloat(rainForecast.get(num)), weatherSummary.get(num), windCondition.get(num));
    Report pmReport = new Report(peakId, maxTemps.get(num+1), minTemps.get(num+1), windChillTemps.get(num+1), Float.parseFloat(snowForecast.get(num+1)), Float.parseFloat(rainForecast.get(num+1)), weatherSummary.get(num+1), windCondition.get(num+1));
    Report nightReport = new Report(peakId, maxTemps.get(num+2), minTemps.get(num+2), windChillTemps.get(num+2), Float.parseFloat(snowForecast.get(num+2)), Float.parseFloat(rainForecast.get(num+2)), weatherSummary.get(num+2), windCondition.get(num+2));

        return new Forecast(amReport,pmReport,nightReport);

    }

    List<Forecast> buildListOfForecasts(Long peakId, List<List<String>> weatherData, int day) {
        List<Forecast> forecastList = new ArrayList<>();

        for(int n = 0; n < day; n++) {
            forecastList.add(buildForecast(peakId,weatherData,day));
        }
        return forecastList;
    }


}
