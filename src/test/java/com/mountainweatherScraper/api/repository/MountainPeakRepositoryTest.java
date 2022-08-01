package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainPeak;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MountainPeakRepositoryTest {
    String[] peakNamesExpected = {"Mount Washington (Nevada)"
            ,"Mount Baker"
            ,"Mount Elbert"
            ,"Mount McKinley"
            ,"Mount Washington (New Hampshire)"
            ,"Mount Shasta","Mount Rainier"
            ,"Mount Whitney"
            ,"K2"
            ,"Bear Mountain (NY)"
            ,"Bear Mountain (California)"
            ,"Mount Washington (Arizona)"};
    List<Long> idArray = new ArrayList<>();
    @Autowired
    MountainPeakRepository repoUnderTest;
    @BeforeEach
    void setUp() {
        //save dummy peak values in test db
        repoUnderTest.save(new MountainPeak("Mount Washington (Nevada)", "/mount-washington-1", "/subranges/snake-range/locations"));
        repoUnderTest.save(new MountainPeak("Mount Baker", "/mount-baker-1", "/subranges/cascade-range-3/locations"));
        repoUnderTest.save(new MountainPeak("Mount Elbert", "/mount-elbert-1", "/subranges/alaska-range/locations"));
        repoUnderTest.save(new MountainPeak("Mount McKinley", "/mount-mckinley-1", ""));
        repoUnderTest.save(new MountainPeak("Mount Washington (New Hampshire)", "/mount-washington-2", "/subranges/white-mountains-new-hampshire/locations"));
        repoUnderTest.save(new MountainPeak("Mount Shasta", "/mount-shasta-1", "/subranges/cascade-range-3/locations"));
        repoUnderTest.save(new MountainPeak("Mount Rainier", "/mount-rainier-1", "/subranges/cascade-range-3/locations"));
        repoUnderTest.save(new MountainPeak("Mount Whitney", "/mount-whitney-1", "/subranges/2662/locations"));
        repoUnderTest.save(new MountainPeak("K2", "/k2-1", "/subranges/karakoram-6/locations"));
        repoUnderTest.save(new MountainPeak("Bear Mountain (NY)", "/bear-mountain-1", "/subranges/catskill-mountains-1/locations"));
        repoUnderTest.save(new MountainPeak("Bear Mountain (California)", "/bear-mountain-2", "/subranges/tehachapi-mountains-1/locations"));
        repoUnderTest.save(new MountainPeak("Mount Washington (Arizona)", "/mount-washington-3", "/subranges/southwest-basins-and-ranges/locations"));

        List<MountainPeak> repoContents = repoUnderTest.getAllPeakNames();
        for(MountainPeak m : repoContents) {
            idArray.add(m.getPeakId());
        }
    }

    @AfterEach
    void tearDown() {
        //remove all entities in prep for next test
        repoUnderTest.deleteAll();
        //delete the data in the id array
        while(!idArray.isEmpty()) {
            idArray.remove(idArray.size()-1);
        }
    }
    //gets up to 10 peaks, not always 10
    @Test
    void getTop10MountainPeakIdByName() {
        List<Map<String, Long>> result1 = repoUnderTest.getTop10MountainPeakIdByName("Mount Washington");
        List<Map<String, Long>> result2 = repoUnderTest.getTop10MountainPeakIdByName("Bear Mountain");
        List<Map<String, Long>> result3 = repoUnderTest.getTop10MountainPeakIdByName("bear mountain");
        List<Map<String, Long>> result4 = repoUnderTest.getTop10MountainPeakIdByName("K2");
        assertThat(result1.size()).isEqualTo(3);
        assertThat(result2.size()).isEqualTo(2);
        assertThat(result3.size()).isEqualTo(2);
        assertThat(result4.size()).isEqualTo(1);
    }

    @Test
    void getPeakUriByPeakId() {
        long peakId = idArray.get(0);
        assertThat(repoUnderTest.getPeakUriByPeakId(peakId)).isEqualTo("/mount-washington-1");
    }

    @Test
    void getPeakNameById() {
        long peakId = idArray.get(0);
        assertThat(repoUnderTest.getPeakNameById(peakId)).isEqualTo("Mount Washington (Nevada)");
    }

    @Test
    void getAllPeakNames() {

        List<MountainPeak> result = repoUnderTest.getAllPeakNames();
        int p = 0;
        for(MountainPeak m : result) {
            assertThat(m.getPeakName()).isEqualTo(peakNamesExpected[p++]);
        }
        }
    }

