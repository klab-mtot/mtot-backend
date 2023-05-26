package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@AllArgsConstructor
public class Locations {
    @Id
    @GeneratedValue()
    @Column(name = "Locations_id")
    private Long id;

    @Column(name = "journey_id",nullable = false)
    private Long journey_id;

    @Column(name = "member_id",nullable = false)
    private Long member_id;

    @Column(name = "updatetime",nullable = false)
    private LocalDateTime createdDate;
    //십진수 도(DD)
    @Column(name = "latitude",nullable = false)
    private double latitude;

    @Column(name = "altitude",nullable = false)
    private double altitude;

    protected Locations() {

    }
}
