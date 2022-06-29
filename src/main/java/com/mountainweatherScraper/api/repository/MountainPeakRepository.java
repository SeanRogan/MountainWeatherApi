package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainPeak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MountainPeakRepository extends JpaRepository<MountainPeak, Long> {
    @Query("SELECT m.uri FROM mountain_peak m WHERE m.peakId = ?1")
    String getPeakUriByPeakId(Long peakId);
    @Query("SELECT m.uri FROM mountain_peak m WHERE m.peakName = ?1")
    String getPeakUriByName(String query);
    @Query("SELECT m.peakName FROM mountain_peak m WHERE m.peakId = ?1")
    String getPeakNameById(Long peakId);
    @Modifying
    @Query("DELETE FROM mountain_peak m where m.peakId < m.peakId AND m.uri = m.uri")
    void deleteDuplicateMountainPeaks();

}
