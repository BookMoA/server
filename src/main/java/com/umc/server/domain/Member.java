package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.domain.mapping.ClubMember;
import com.umc.server.domain.mapping.ClubPostComment;
import com.umc.server.domain.mapping.ClubPostLike;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 생성하지만 접근 수준을 protected로 제한
@DynamicUpdate
@DynamicInsert
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean inFocusMode;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long totalPages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpType signUpType;

    private String accessToken;

    private String profileURL;

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isDeleted;

    private LocalDate inActiveDate;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private PushNotification pushNotification;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private ClubMember clubMember;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubPost> clubPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubPostComment> clubPostCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubPostLike> clubPostLikeList = new ArrayList<>();
}
