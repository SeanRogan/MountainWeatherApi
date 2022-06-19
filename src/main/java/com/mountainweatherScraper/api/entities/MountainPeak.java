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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long peakId;
    @Column
    private String peakName;
    @Column
    private String homeSubrangeUri;
    @Column
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
