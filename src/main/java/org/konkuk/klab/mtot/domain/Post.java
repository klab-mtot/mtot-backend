package org.konkuk.klab.mtot.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {
    @Id @GeneratedValue()
    @Column(name = "post_id")
    private Long id;

    //@Column(name = "Journey", nullable = false)
    //private long journey_id;

    @Column(name = "createdTime", nullable = false)
    private LocalDateTime createdDatetime;

    @Column(name = "updateTime", nullable = false)
    private LocalDateTime updateDatetime;

    @Setter
    @Column(name = "post")
    private String postHtml;



//    public Post(Long journey_id){
//        this.journey_id = journey_id;
//        //생성된 시간 추가 필요할시에 마저 구현
//    }
}
