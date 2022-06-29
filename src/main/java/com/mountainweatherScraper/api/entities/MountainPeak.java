package com.mountainweatherScraper.api.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.io.Serializable;

@Entity(name="mountain_peak")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MountainPeak implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long peakId;
    @Column(unique=true)
    private String peakName;
    @Column(unique=true)
    private String homeSubrangeUri;

    @Column(unique=true)
    private Long rangeId;

    @ManyToOne
    private SubRange homeSubrange;
    @ManyToOne
    private MountainRange homeRange;
    @Column
    private String uri;
    public MountainPeak(String peakName, String uri, String subRangeUri) {
        this.peakName = peakName;
        this.uri = uri;
        this.homeSubrangeUri = subRangeUri;

    }
}
