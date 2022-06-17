package com.mountainweatherScraper.api.service;

import javax.servlet.http.HttpServletRequest;
//todo this class needs to parse incoming requests to determine what data service needs to be invoked
public class RequestHandlerService {
    HttpServletRequest request;
    public RequestHandlerService(HttpServletRequest request) {
        this.request = request;
    }


}
