package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.domain.mapping.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@Builder
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
    private Boolean inFocusMode;

    @ColumnDefault("0")
    private Long totalPages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpType signUpType;

    private String accessToken;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String profileURL;

    @ColumnDefault("false")
    private Boolean isDeleted;

    private LocalDate inActiveDate;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private PushNotification pushNotification;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private ClubMember clubMember;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPost> clubPostList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPostComment> clubPostCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPostLike> clubPostLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberBook> memberBookList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BookList> bookList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberBookList> memberBookListList = new ArrayList<>();
}
