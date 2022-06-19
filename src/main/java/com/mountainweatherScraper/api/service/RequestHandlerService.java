package com.mountainweatherScraper.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
//todo this class needs to parse incoming requests to determine what data service needs to be invoked
public class RequestHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerService.class);
    HttpServletRequest request;
    public RequestHandlerService(HttpServletRequest request) {

        this.request = request;
    }


}
