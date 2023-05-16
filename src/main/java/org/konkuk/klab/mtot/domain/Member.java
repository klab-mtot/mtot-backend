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
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
