package com.mountainweatherScraper.api.util;

import com.mountainweatherScraper.api.service.DatabaseInitializerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.stereotype.Component;

/**
 * @deprecated
 * The StartUpInitializer class handles the initial db population on application start
 */
@Component
public class StartUpInitializer {

    private final Logger logger = LoggerFactory.getLogger(StartUpInitializer.class);
    private final DatabaseInitializerService ds;
    @Autowired
    public StartUpInitializer(DatabaseInitializerService ds) {
        this.ds = ds;
    }

    //to use this, use @Autowired
    public void initOnTrafficAcceptance(AvailabilityChangeEvent<ReadinessState> event) {
        if(event.getState().equals(ReadinessState.ACCEPTING_TRAFFIC)) {
            logger.info("Server is started\nReadinessState: ACCEPTING_TRAFFIC");
            ds.init();
            //ds.clean();
        }
    }
}
