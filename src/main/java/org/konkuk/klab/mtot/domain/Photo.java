package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {
    @Id @GeneratedValue()
    @Column(name = "photo_id")
    private Long id;


    //@Column(name = "journey_id",nullable = false)
    //private  long journey_id;

    @Column(name = "filename", nullable = false)
    private String name;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;



}
