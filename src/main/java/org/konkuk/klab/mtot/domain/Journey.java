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
public class Journey {

    @Id @GeneratedValue()
    @Column(name = "journey_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(mappedBy = "journey")
    private Post post;

    @OneToMany(mappedBy = "journey")
    private List<Photo> photos = new ArrayList<>();

    public Journey(Team team, String journeyName) {
        this.name = journeyName;
        this.team = team;
        team.getJourneyList().add(this);
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
