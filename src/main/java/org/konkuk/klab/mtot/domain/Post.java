package org.konkuk.klab.mtot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    private Long id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "journey_id")
    private Journey journey;

    private String title;

    private String article;

    public Post(Journey journey, String title, String article) {
        journey.setPost(this);
        this.journey = journey;
        this.title = title;
        this.article = article;
    }

    public void edit(String title, String article) {
        this.title = title;
        this.article = article;
    }
}
