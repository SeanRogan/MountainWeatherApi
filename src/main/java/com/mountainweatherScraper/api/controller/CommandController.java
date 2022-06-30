package com.mountainweatherScraper.api.controller;

import com.mountainweatherScraper.api.service.DatabaseInitializerService;
import com.mountainweatherScraper.api.service.WeatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CommandController {


//this is hacky, but its the only way i can initialize the DB after deployment startup,
// without it trying to replicate things on every start.
    private final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private final DatabaseInitializerService ds;
    @Autowired
    public CommandController(DatabaseInitializerService ds) {
        this.ds = ds;
    }
    @GetMapping("/initialize")
    public ModelAndView init(HttpServletRequest request) {
        if(request.getHeader("password").equals("thesecretinitializationpassword"))
        {
            logger.info("initializing database");
            ds.init();
        }
        return new ModelAndView("home-view");
    }
}
