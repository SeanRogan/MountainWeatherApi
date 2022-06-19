package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MountainRangeRepository extends JpaRepository<MountainRange, Long> {

    @Query("SELECT mountain_ranges, mountain_ranges.rangeId FROM mountain_ranges ")
    Map<MountainRange,Long> getAllRanges();
    @Query("SELECT r FROM mountain_ranges r WHERE r.uri = ?1 AND r.rangeName = ?2")
    Boolean findIfRangeExists(String uri, String rangeName);


}
