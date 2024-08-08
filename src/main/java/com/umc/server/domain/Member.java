package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.Role;
import com.umc.server.domain.enums.SignUpType;
import com.umc.server.domain.mapping.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@Builder
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    @Setter
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

    @Column(nullable = false, length = 20)
    @Setter
    private String nickname;

    @ColumnDefault("false")
    private Boolean inFocusMode;

    @ColumnDefault("0")
    private Long totalPages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpType signUpType;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Setter
    private String kakaoAccessToken;

    private Long kakaoId;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Setter
    private String refreshToken;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Setter
    private String profileURL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @Setter
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> roles = new ArrayList<>();
        String authority = this.role.getAuthority();
        roles.add(new SimpleGrantedAuthority(authority));
        return roles;
    }

    @Override
    public String getUsername() {
        return this.nickname;
    }
}
