package com.mountainweatherScraper.api.repository;

import com.mountainweatherScraper.api.entities.SubRange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class SubRangeRepositoryTest {
    @Autowired
    SubRangeRepository repoUnderTest;
    @BeforeEach
    void setUp() {
        repoUnderTest.save(new SubRange("testRange",
                "/test-range",
                "/ranges/test-range-parent/locations"));
    }
    @Test
    void testRepo() {
        List<SubRange> repoContent = repoUnderTest.findAll();
        if(repoContent.size() != 1) {
            fail();
        }

    }
    @AfterEach
    void tearDown() {
        repoUnderTest.deleteAll();
    }

}