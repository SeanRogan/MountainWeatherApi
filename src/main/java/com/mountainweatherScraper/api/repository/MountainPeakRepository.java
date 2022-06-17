package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainPeak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MountainPeakRepository extends JpaRepository<MountainPeak, Long> {
    @Query("SELECT m FROM mountain_peak m WHERE m.peakId = ?1 AND m.rangeId = ?2" )
    MountainPeak getPeakByPeakAndRangeId(Long peakId, Long rangeId);
    @Query("SELECT m.uri FROM mountain_peak m WHERE m.peakId = ?1 AND m.rangeId = ?2")
    String getPeakUriByPeakAndRangeId(Long peakId, Long rangeId);

    @Query("SELECT m FROM mountain_peak m WHERE m.homeState = ?1")
    Map<Long,MountainPeak> getAllMountainsByHomeState(String state);
}
