package com.mountainweatherScraper.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class DocumentationViewController {

    @GetMapping("/documentation")
    ModelAndView documentationView() {
        ModelAndView mav = new ModelAndView("documentation-view");
        return mav;
    }
}
