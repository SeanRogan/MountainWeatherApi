package com.mountainweatherScraper.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


//todo FOR FUTURE USE WHEN SECURITY IS IMPLEMENTED
// this class needs to parse incoming requests to determine if theyre valid,
// and should handle routing to security
@Service
public class RequestHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerService.class);
    HttpServletRequest request;
    @Autowired
    public RequestHandlerService(HttpServletRequest request) {

        this.request = request;
    }

    /**
     *
     */
    public ResponseEntity<String> authorize() {
        String requestHeaders = request.getHeader("Authorization");
        ResponseEntity<String> error = new ResponseEntity<>("error : \"access denied\"", HttpStatus.UNAUTHORIZED);

        if(null != requestHeaders) return null;

        return error;
    }
}
