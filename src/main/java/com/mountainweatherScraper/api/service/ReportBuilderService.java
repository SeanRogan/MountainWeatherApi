package com.mountainweatherScraper.api.service;

import com.google.gson.JsonObject;
import com.mountainweatherScraper.api.webscraper.DataScraper;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ReportBuilderService {

    DataService service =  new DataService(new DataScraper());
    private String mountainId;
    private String rangeId;
    public ReportBuilderService(String mountainId,String rangeId) {
        this.mountainId = mountainId;
        this.rangeId = rangeId;

    }

    public JsonObject createReport() {

    }


}
