package com.mountainweatherScraper.api.service;

import com.mountainweatherScraper.api.entities.MountainPeak;
import com.mountainweatherScraper.api.repository.MountainPeakRepository;
import com.mountainweatherScraper.api.util.SearchFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    private MountainPeakRepository testPeakRepo;
    private AutoCloseable autoCloseable;
    @Mock
    private SearchFormatter searchFormatter;
    private SearchService serviceUnderTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        testPeakRepo.save(new MountainPeak("Mount Washington","/test-uri","subrangetesturi"));
        serviceUnderTest = new SearchService(testPeakRepo,searchFormatter);
    }

    @Test
    void searchForMountainPeak() {
        ResponseEntity<String> response = serviceUnderTest.searchForMountainPeak("mount+washington");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        if(response.hasBody() && response.getBody() != null) {
            assertFalse(response.getBody().isEmpty());
        }
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    testPeakRepo.deleteAll();
    }
}