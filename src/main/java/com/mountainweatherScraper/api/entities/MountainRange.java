package com.mountainweatherScraper.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;


@Entity(name = "mountain_ranges")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MountainRange implements Serializable {
    @Id //for primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "range_id")

    private Long rangeId;

    @Column(name = "range_name")
    private String rangeName;

    @Column(name = "uri")

    private String uri;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "subrangeId")
    private Set<SubRange> subRanges;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "peakId")
    private Set<MountainPeak> peaks;


    public MountainRange(String rangeName, String uri, Set<SubRange> subRanges, Set<MountainPeak> peaks) {
        this.rangeName = rangeName;
        this.uri = uri;
        this.subRanges = subRanges;
        this.peaks = peaks;
    }
}
