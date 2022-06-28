package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainRange;
import com.mountainweatherScraper.api.entities.SubRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRangeRepository extends JpaRepository<SubRange, Long> {
    @Query("SELECT r FROM mountain_ranges r WHERE r.uri = ?1 AND r.rangeName = ?2")
    Boolean findIfSubRangeExists(String uri, String rangeName);
//finds the peaks of a subrange by the subrange id
    @Query("SELECT r.peaks FROM sub_ranges r WHERE r.subrangeId = ?1")
    List<Long> getAllPeakIdsInSubRange(Long subRangeId);
        //possibly needed methods
    //getParentRangeOfSubRange

        @Modifying
        @Query("DELETE FROM sub_ranges r where r.subrangeId < r.subrangeId AND r.uri = r.uri")
        void deleteDuplicateSubRanges();

}
