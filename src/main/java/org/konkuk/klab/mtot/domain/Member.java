package org.konkuk.klab.mtot.domain;

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
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "email", nullable = false)
    String email;

    @JsonManagedReference
    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
