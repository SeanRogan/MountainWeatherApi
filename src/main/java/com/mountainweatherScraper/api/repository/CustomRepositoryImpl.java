package com.mountainweatherScraper.api.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomRepositoryImpl implements CustomRepository {

@PersistenceContext
EntityManager manager;
    @Override
    public List getTop10MountainPeakIdByName(String query) {
        return manager.createNativeQuery("SELECT m.peak_name, peak_id FROM mountain_peak m WHERE m.peak_name ILIKE CONCAT(:q,'%');").getResultList();

    }
}
