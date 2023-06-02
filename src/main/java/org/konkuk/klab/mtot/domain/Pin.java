package org.konkuk.klab.mtot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"id", "member", "journey", "photos"})
public class Pin {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    @Column(name = "location")
    private Location location;

    @Column(name = "createdTime")
    private LocalDateTime createdTime;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "journey_id")
    private Journey journey;

    @JsonManagedReference
    @OneToMany(mappedBy = "pin")
    private List<Photo> photos = new ArrayList<>();

    public Pin(Member member, Journey journey, Location location){
        this.member = member;
        this.journey = journey;
        this.location = location;
        this.createdTime = LocalDateTime.now();
        journey.getPins().add(this);
    }
}
