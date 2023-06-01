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
    private Long id;

    @Column(name = "accept")
    private boolean isAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id")
    private Member receiver;

    public Friendship(Member requester, Member receiver){
        this.requester = requester;
        this.receiver = receiver;
        this.isAccepted = false;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
