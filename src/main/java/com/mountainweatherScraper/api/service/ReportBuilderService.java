package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.mountainweatherScraper.api.entities.Report;
import com.mountainweatherScraper.api.model.Forecast;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.repository.ReportRepository;
import com.mountainweatherScraper.api.webscraper.DataScraper;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportBuilderService {
//todo need to figure out how to put the three reports together into a forecast
    MountainPeakRepository peakRepo;
    ReportRepository reportRepo;
    DataService dataService =  new DataService(new DataScraper());
    public ReportBuilderService() {
    }

    public ResponseEntity<String> createReportResponse(Long peakId, Long rangeId) {
        Forecast weatherForecast = buildForecast(peakId, dataService.getWeatherData(
                peakRepo.getPeakUriByPeakAndRangeId(
                        peakId,rangeId)));

        Gson g = new Gson();
        ResponseEntity<String> response = new ResponseEntity<>(g.toJson(weatherForecast), HttpStatus.OK);

        return response;

    }
//todo this is insane how im doing this but itll work
// just to get everything up and running then well improve.
// should be done with streams
    private Forecast buildForecast(Long peakId, List<List<String>> weatherData) {
        //max temps, min, chill, snow, rain weather, wind
        List<String> maxTemps = weatherData.get(0);
        List<String> minTemps = weatherData.get(1);
        List<String> windChillTemps = weatherData.get(2);
        List<String> snowForecast = weatherData.get(3);
        List<String> rainForecast = weatherData.get(4);
        List<String> weatherSummary = weatherData.get(5);
        List<String> windCondition = weatherData.get(6);

        Report amReport = new Report(peakId, maxTemps.get(0),minTemps.get(0),windChillTemps.get(0),Float.parseFloat(snowForecast.get(0)),Float.parseFloat(rainForecast.get(0)),weatherSummary.get(0),windCondition.get(0));
        Report pmReport = new Report(peakId, maxTemps.get(0),minTemps.get(0),windChillTemps.get(0),Float.parseFloat(snowForecast.get(0)),Float.parseFloat(rainForecast.get(0)),weatherSummary.get(0),windCondition.get(0));
        Report nightReport = new Report(peakId, maxTemps.get(0),minTemps.get(0),windChillTemps.get(0),Float.parseFloat(snowForecast.get(0)),Float.parseFloat(rainForecast.get(0)),weatherSummary.get(0),windCondition.get(0));

        return new Forecast(amReport,pmReport,nightReport);

    }
}
