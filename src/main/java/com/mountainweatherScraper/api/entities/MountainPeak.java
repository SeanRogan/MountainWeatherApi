package com.mountainweatherScraper.api.entities;


import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name="mountain_peak")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MountainPeak implements Serializable {

    public MountainPeak(String peakName, String uri) {
        this.peakName = peakName;
        this.uri = uri;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @MapsId
    @Column(name = "peak_id")
    private Long peakId;
    @Column(name = "peak_name")
    private String peakName;

    @Column(name = "home_state")
    private String homeState;

    @Column(name = "range_id")

    private Long rangeId;
    @Column(name = "uri")

    private String uri;
}
