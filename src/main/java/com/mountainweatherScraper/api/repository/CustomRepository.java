package com.mountainweatherScraper.api.repository;

import java.util.List;
import java.util.Map;

public interface CustomRepository {
    List<Map<String,Long>> getTop10MountainPeakIdByName(String query);
}
