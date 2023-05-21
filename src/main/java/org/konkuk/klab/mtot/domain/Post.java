package org.konkuk.klab.mtot.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "Journey",nullable = false)
    private long journey_id;

    @Column(name = "createdtime",nullable = false)
    private LocalDateTime createddatetime;

    @Column(name = "updatetime",nullable = false)
    private LocalDateTime updatedatetime;

    @Column(name = "post")
    private String postHtml;

}
