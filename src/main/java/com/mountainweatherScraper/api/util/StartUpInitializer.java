package com.mountainweatherScraper.api.util;

import com.mountainweatherScraper.api.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class StartUpInitializer {

    private final Logger logger = LoggerFactory.getLogger(StartUpInitializer.class);
    private final DataService ds;
    @Autowired
    public StartUpInitializer(DataService ds) {
        this.ds = ds;
    }
    @EventListener
    public void initOnTrafficAcceptance(AvailabilityChangeEvent<ReadinessState> event) {
        if(event.getState().equals(ReadinessState.ACCEPTING_TRAFFIC)) {
            logger.info("Server is started\nReadinessState: ACCEPTING_TRAFFIC");
            ds.init();
            //ds.clean();
        }
    }
}
