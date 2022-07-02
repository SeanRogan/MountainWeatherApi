package com.mountainweatherScraper.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeViewController {

    @GetMapping({"/","/home"})
    ModelAndView homeView() {
        ModelAndView mav = new ModelAndView("home-view");
        return mav;
    }

}

