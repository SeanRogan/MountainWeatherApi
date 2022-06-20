package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MountainRangeRepository extends JpaRepository<MountainRange, Long> {

    //getAllSubRangesByRangeId

    //getAllPeaksInRangeByRangeId




}
