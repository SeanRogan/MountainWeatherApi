package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainPeak;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MountainPeakRepository extends JpaRepository<MountainPeak, Long>, CustomRepository {
    @Query(value = "SELECT m.peak_name, peak_id FROM mountain_peak m WHERE m.peak_name ILIKE CONCAT(:q,'%') ;",nativeQuery = true)
    List<Map<String,Long>> getTop10MountainPeakIdByName(@Param("q") String query);
    @Query("SELECT m.uri FROM mountain_peak m WHERE m.peakId = ?1")
    String getPeakUriByPeakId(Long peakId);
    @Query("SELECT m.uri FROM mountain_peak m WHERE m.peakName = ?1")
    String getPeakUriByName(String query);
    @Query("SELECT m.peakName FROM mountain_peak m WHERE m.peakId = ?1")
    String getPeakNameById(Long peakId);

}
