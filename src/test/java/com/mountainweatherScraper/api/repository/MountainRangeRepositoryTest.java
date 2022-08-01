package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.MountainPeak;
import com.mountainweatherScraper.api.entities.MountainRange;
import com.mountainweatherScraper.api.entities.SubRange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class MountainRangeRepositoryTest {
    @Autowired
    MountainRangeRepository repoUnderTest;
    @BeforeEach
    void setUp() {
        Set<SubRange> testSubRangeSet = new HashSet<>();
        testSubRangeSet.add(new SubRange());
        Set<MountainPeak> testPeakSet = new HashSet<>();
        testPeakSet.add(new MountainPeak());

        MountainRange testRange = new MountainRange("testRange", "/test-uri", testSubRangeSet, testPeakSet);
        repoUnderTest.save(testRange);
    }

    @Test
    void findIfRangeExists() {
        repoUnderTest.findIfRangeExists("/test-uri" , "testRange");
    }

    @AfterEach
    void tearDown() {
        repoUnderTest.deleteAll();
    }
}