package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MountainRangeRepository extends JpaRepository<MountainRange, Long> {


    @Query("SELECT r.rangeName FROM sub_ranges r WHERE r.uri = ?1 AND r.rangeName = ?2")
    String findIfRangeExists(String uri, String rangeName);
    //getAllSubRangesByRangeId

    //getAllPeaksInRangeByRangeId


}
