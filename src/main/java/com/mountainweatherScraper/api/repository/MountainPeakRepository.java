package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainPeak;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MountainPeakRepository extends PagingAndSortingRepository<MountainPeak, Long>,  CustomRepository {
    @Query(value = "SELECT m.peak_name, peak_id FROM mountain_peak m WHERE m.peak_name ILIKE CONCAT(:q,'%') ;" , nativeQuery = true)
    List<Map<String,Long>> getTop10MountainPeakIdByName(@Param("q") String query);
    @Query("SELECT m.uri FROM mountain_peak m WHERE m.peakId = ?1")
    String getPeakUriByPeakId(Long peakId);
    @Query("SELECT m.peakName FROM mountain_peak m WHERE m.peakId = ?1")
    String getPeakNameById(Long peakId);
    @Query("SELECT m FROM mountain_peak m")
    List<MountainPeak> getAllPeakNames();
    @Query("SELECT m FROM mountain_peak m WHERE m.peakName LIKE 'Mount Washington(New Hampshire)' OR m.peakName LIKE  'Mount Katahdin' OR m.peakName LIKE  'Mount Whitney' OR m.peakName LIKE  'Mount McKinley' OR m.peakName LIKE  'Grand Teton' OR m.peakName LIKE  'Mount Rainier' OR m.peakName LIKE  'Mount Shasta' OR m.peakName LIKE  'Mount Baker' OR m.peakName LIKE  'Mount Elbert' OR m.peakName LIKE  'K2'")
    Page<MountainPeak> findAllDemoPeaks(Pageable pageable);
}
