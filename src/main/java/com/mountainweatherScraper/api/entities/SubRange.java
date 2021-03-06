package com.mountainweatherScraper.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;


@Entity(name = "sub_ranges")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubRange implements Serializable {
    @Id //for primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subrange_id",unique=true)
    private Long subrangeId;
    @Column(name = "home_range_uri", unique=true)
    private String homeRangeUri;
    @Column(name = "range_name", unique=true)
    private String rangeName;
    @Column(name = "uri", unique=true)
    private String uri;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "peakId")
    private Set<MountainPeak> peaks;


    public SubRange(String rangeName, String uri, String homeRangeUri) {
        this.rangeName = rangeName;
        this.homeRangeUri = homeRangeUri;
        this.uri = uri;
    }
}
