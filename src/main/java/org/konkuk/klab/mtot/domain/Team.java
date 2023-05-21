package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "leader_id", nullable = false)
    private Long leaderId;



    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();
z
    @OneToMany(mappedBy = "team")
    private List<Journey> journeyList = new ArrayList<>();

    public Team(String name, Long leaderId) {
        this.name = name;
        this.leaderId = leaderId;
    }
}
