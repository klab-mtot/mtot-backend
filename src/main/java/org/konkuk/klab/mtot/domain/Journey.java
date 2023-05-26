package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journey {
    @Id @GeneratedValue()
    @Column(name = "journey_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "team_id")
    private Long team_id;//many to one

    @Column(name = "post_id")
    private Long post_id;//one to one

    @OneToMany(mappedBy = "journey")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "journey")
    private List<Locations> locations = new ArrayList<>();

    public Journey(String name, Long team_id) {
        this.team_id = team_id;
        this.name = name;
        this.post_id = new Post().getId();
    }
    public void setPost(long post_id){
        this.post_id = post_id;
    }


}



