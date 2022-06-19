package com.mountainweatherScraper.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//todo this class needs to parse incoming requests to determine what data service needs to be invoked
public class RequestHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerService.class);
    HttpServletRequest request;
    public RequestHandlerService(HttpServletRequest request) {

        this.request = request;
    }


    public ResponseEntity<String> authorize() {
        String requestHeaders = request.getHeader("Authorization");
        ResponseEntity<String> error = new ResponseEntity<>("error : \"access denied\"", HttpStatus.UNAUTHORIZED);

        if(null != requestHeaders) return null;

        return error;
    }
}
