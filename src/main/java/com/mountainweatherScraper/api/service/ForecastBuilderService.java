package com.mountainweatherScraper.api.service;

import com.google.gson.Gson;
import com.mountainweatherScraper.api.model.Forecast;
import com.mountainweatherScraper.api.model.Report;
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

//todo implement the builder method in this class to break up the responsibilities
// of the buildForecast method and to make unit testing the createWeatherReportResponse method possible.

/**
 * The ForecastBuilderService class is a service class, it contains the business logic responsible
 * for building weather forecasts and
 */
@Service
public class ForecastBuilderService{
    private static final Logger logger = LoggerFactory.getLogger(ForecastBuilderService.class);
    private final MountainPeakRepository peakRepo;
    private final WeatherDataService weatherDataService;
    private final Gson gson;





    @Autowired
    public ForecastBuilderService(MountainPeakRepository peakRepo, WeatherDataService weatherDataService) {
        this.peakRepo = peakRepo;
        this.weatherDataService = weatherDataService;
        this.gson = new Gson();
    }

    /**
     * createWeatherReportResponse method takes a Long number as peakId ,
     * an integer 'numberOfDays' representing a 1 or 6 day report being requested,
     * and string which was the temp-format header of the request.
     * @param peakId
     * @param numberOfDays
     * @param tempFormat
     * @return Returns a responseEntity<String> object with the forecast requested, or an error message if something went wrong in the service class.
     */


    public ResponseEntity<String> createWeatherReportResponse(Long peakId, int numberOfDays, String tempFormat) {
        //validate peak id by attempting a uri lookup, if the string is null after lookup, the peak isnt in the database.
        String uri = peakRepo.getPeakUriByPeakId(peakId);
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        //if id check is not null, create weather report, else return an error message
        if (uri != null) {
            //collect weather data
            List<Forecast> weatherForecast = buildListOfForecasts(peakId,
                    weatherDataService
                            .getWeatherData(uri,
                                    tempFormat),
                    numberOfDays);
            String responseBody = gson.toJson(weatherForecast);
            response = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            logger.info("created responseEntity : \n" + response + "\n");
            return response;
        }
        //if the application gets this far the idCheck must have been null, so send an error response.
        String badIdResponseBody = "Sorry, but that peak id is not associated with any mountain. Please double check the peak id is correct and try again.";
        response = new ResponseEntity<>(badIdResponseBody, headers, HttpStatus.NOT_FOUND);
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
    private Forecast buildForecast(Long peakId, List<List<String>> weatherData , int num) {
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
        Collections.replaceAll(snowForecast, "—","0.0");
        Collections.replaceAll(rainForecast, "—","0.0");
        logger.trace("creating AM report for day" + num);
        Report amReport = new Report.ReportBuilder()
                .name(peakRepo.getPeakNameById(peakId))
                .day(dayAndDate.get(num))
                .high(maxTemps.get(num))
                .low(minTemps.get(num))
                .chill(windChillTemps.get(num))
                .snow(Float.parseFloat(snowForecast.get(num)))
                //todo this will throw IOB error if theres no rain forecast. probably same with snow above. i think every mountain will have either one or the other so this could potentially be the cause of all errors
                .rain(Float.parseFloat(rainForecast.get(num)))
                .weatherConditions(weatherSummary.get(num))
                .wind(windCondition.get(num)).build();
        logger.trace("creating PM report for day" + num);
        Report pmReport = new Report.ReportBuilder()
                .name(peakRepo.getPeakNameById(peakId))
                .day(dayAndDate.get(num))
                .high(maxTemps.get(num+1))
                .low(minTemps.get(num+1))
                .chill(windChillTemps.get(num+1))
                .snow(Float.parseFloat(snowForecast.get(num+1)))
                .rain(Float.parseFloat(rainForecast.get(num+1)))
                .weatherConditions(weatherSummary.get(num+1))
                .wind(windCondition.get(num+1)).build();
        logger.trace("creating NIGHT report for day" + num);
        Report nightReport = new Report.ReportBuilder()
                .name(peakRepo.getPeakNameById(peakId))
                .day(dayAndDate.get(num))
                .high(maxTemps.get(num+2))
                .low(minTemps.get(num+2))
                .chill(windChillTemps.get(num+2))
                .snow(Float.parseFloat(snowForecast.get(num+2)))
                .rain(Float.parseFloat(rainForecast.get(num+2)))
                .weatherConditions(weatherSummary.get(num+2))
                .wind(windCondition.get(num+2)).build();
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
    private List<Forecast> buildListOfForecasts(Long peakId, List<List<String>> weatherData, int days) {
        List<Forecast> forecastList = new ArrayList<>();
        for(int i = 0; i < days; i++) {
            forecastList.add(buildForecast(peakId, weatherData, i));
        }
        return forecastList;
    }




}
