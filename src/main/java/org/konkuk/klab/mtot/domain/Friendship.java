package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {
    @Id @GeneratedValue
    @Column(name = "friendship_id")
    Long id;
    @Column(name = "accept", columnDefinition = "boolean default false")
    private boolean accept;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="requester_id", referencedColumnName = "member_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id", referencedColumnName = "member_id")
    private Member receiver;

    public Friendship(Member requester, Member receiver){
        this.requester = requester;
        this.receiver = receiver;
    }
}
