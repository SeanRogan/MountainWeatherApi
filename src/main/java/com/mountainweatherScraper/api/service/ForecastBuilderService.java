package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.mountainweatherScraper.api.model.DayOfTheWeek;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Service
public class ForecastBuilderService{
DayOfTheWeek weekdays;
    private static final Logger logger = LoggerFactory.getLogger(ForecastBuilderService.class);

    MountainPeakRepository peakRepo;
    WeatherDataService weatherDataService;
    @Autowired
    public void setDataService(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
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

    //todo i think this method should logically go in its own class, a ResponseBuilderService class.
    public ResponseEntity<String> createWeatherReportResponse(Long peakId, int numberOfDays, String tempFormat) {
        Gson g = new Gson();
        List<Forecast> weatherForecast = buildListOfForecasts(peakId,
                weatherDataService
                        .getWeatherData(peakRepo.getPeakUriByPeakId(peakId),
                                tempFormat),
                                numberOfDays);

        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();
        //set headers
        String responseBody = g.toJson(weatherForecast);
        ResponseEntity<String> response;
        response = new ResponseEntity<>(responseBody, headers, status);
        logger.info("created responseEntity : \n" + response +"\n");
        return response;

    }
    /**
     * @param peakId the id of the mountain peak.
     * @param weatherData a list of lists of strings,
     *                    each list containing a category of data needed by the forecast service;
     *                    high, low, windchill, rain, snow, wind, weather summary,
     *                    and the day of the week and date of the forecast.
     * @param num the number of days the forecast should contain. normally 1 or 6.
     *
     * the build forecast method creates 3 Report objects from the scraped weather data passed to it.
     *            It then returns a Forecast object containing the 3 reports.
     *
     * @return Forecast object containing 3 reports, AM, PM and NIGHT,
     * for the mountain specified by the peak id argument.
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
        List<String> dayAndDate = weatherData.get(7);
        logger.trace("replacing: - with: 0.0 in precipitation forecasts");
        Collections.replaceAll(snowForecast, "-","0.0");
        Collections.replaceAll(rainForecast, "-","0.0");
        logger.trace("creating AM report for day" + num);
        Report amReport = new Report(peakRepo.getPeakNameById(peakId),
                dayAndDate.get(num),
                maxTemps.get(num),
                minTemps.get(num),
                windChillTemps.get(num),
                Float.parseFloat(snowForecast.get(num)),
                Float.parseFloat(rainForecast.get(num)),
                weatherSummary.get(num),
                windCondition.get(num));
        logger.trace("creating PM report for day" + num);
        Report pmReport = new Report(peakRepo.getPeakNameById(peakId),
                dayAndDate.get(num),
                maxTemps.get(num+1),
                minTemps.get(num+1),
                windChillTemps.get(num+1),
                Float.parseFloat(snowForecast.get(num+1)),
                Float.parseFloat(rainForecast.get(num+1)),
                weatherSummary.get(num+1),
                windCondition.get(num+1));
        logger.trace("creating NIGHT report for day" + num);
        Report nightReport = new Report(peakRepo.getPeakNameById(peakId),
                dayAndDate.get(num),
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
     * @param peakId - the peak id number
     * @param weatherData - A List containing Lists of Strings,
     *                   each inner List containing one of the
     *                    collected weather data categories(high,low,windchill temps, etc)
     * @param days - the number of days the weather forecast should cover(normally one day or six days)\
     *
     * @return List<Forecast> a list of forecasts,one for each day specifies,
     * each forecast containing 3 weather reports for the day.
     */
    List<Forecast> buildListOfForecasts(Long peakId, List<List<String>> weatherData, int days) {
        List<Forecast> forecastList = new ArrayList<>();

        for(int n = 0; n < days; n++) {
            forecastList.add(buildForecast(peakId, weatherData, n));
        }
        return forecastList;
    }


}
