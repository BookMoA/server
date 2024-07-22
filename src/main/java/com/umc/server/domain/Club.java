package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.mapping.ClubMember;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String intro;

    @Column(nullable = false, length = 200)
    private String notice;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(length = 10)
    private String password;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubMember> clubMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPost> clubPostList = new ArrayList<>();
}
