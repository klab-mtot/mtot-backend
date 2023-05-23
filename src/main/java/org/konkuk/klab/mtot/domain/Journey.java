package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journey {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "journey_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "team_id")
    private Long team_id;

    @Column(name = "post_id")
    private Long post_id;

    @OneToMany(mappedBy = "journey")
    private List<Photo> photos = new ArrayList<>();
    @OneToMany(mappedBy = "journey")
    private List<Locations> locations = new ArrayList<>();

    public Journey(String name, Long team_id) {
        this.team_id = team_id;
        this.name = name;
        //포스트 생성 방식은 좀 있다가 구현
    }
    public void setPost(long post_id){
        this.post_id = post_id;
    }


}



