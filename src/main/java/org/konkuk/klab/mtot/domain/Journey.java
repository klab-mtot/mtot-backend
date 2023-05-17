package org.konkuk.klab.mtot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Journey {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private final Team team;

    @OneToMany(mappedBy = "journey")
    private List<MemberTeam> posts = new ArrayList<>();
    @OneToMany(mappedBy = "journey")
    private List<MemberTeam> photos = new ArrayList<>();
    @OneToMany(mappedBy = "journey")
    private List<MemberTeam> locations = new ArrayList<>();


}



