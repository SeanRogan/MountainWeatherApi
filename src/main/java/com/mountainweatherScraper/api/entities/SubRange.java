package com.mountainweatherScraper.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    @MapsId
    @Column(name = "subrange_id")
    private Long subrangeId;
    @Column(name = "home_range_id")
    private Long homeRangeId;
    @Column(name = "range_name")
    private String rangeName;

    @Column(name = "uri")
    private String uri;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "peak_id")
    private Set<MountainPeak> peaks;


    public SubRange(String rangeName, Long homeRangeId, String uri) {
        this.rangeName = rangeName;
        this.homeRangeId = homeRangeId;
        this.uri = uri;
    }
}
