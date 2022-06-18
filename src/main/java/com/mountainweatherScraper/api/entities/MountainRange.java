package com.mountainweatherScraper.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "subrange_id")
    private Set<SubRange> subRanges;

    public Set<SubRange> getSubRanges() {
        return subRanges;
    }

    public void setSubRanges(Set<SubRange> subRanges) {
        this.subRanges = subRanges;
    }

    //@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    //@JoinColumn(name = "peak_id")
    //private Set<MountainPeak> peaks;
    public MountainRange(String rangeName, String uri) {
        this.rangeName = rangeName;
        this.uri = uri;
    }
}
