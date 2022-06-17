package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ReportRepository extends JpaRepository<Long, String> {
    @Query("SELECT r FROM Report r WHERE r.peakId=?1")
    Report getReportByPeakAndRangeId(Long peakId);
}
