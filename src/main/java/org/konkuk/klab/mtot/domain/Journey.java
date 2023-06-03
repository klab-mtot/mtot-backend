package org.konkuk.klab.mtot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journey {

    @Id @GeneratedValue()
    @Column(name = "journey_id")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonManagedReference
    @OneToOne(mappedBy = "journey")
    private Post post;

    @JsonManagedReference
    @OneToMany(mappedBy = "journey")
    private List<Pin> pins = new ArrayList<>();

    public Journey(Team team, String journeyName) {
        this.name = journeyName;
        this.team = team;
        team.getJourneyList().add(this);
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
