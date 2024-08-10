package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.mapping.ClubPostComment;
import com.umc.server.domain.mapping.ClubPostLike;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@Builder
public class ClubPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String context;

    @OneToMany(mappedBy = "clubPost", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPostComment> clubPostCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "clubPost", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPostLike> clubPostLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "clubPost", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClubPostPhoto> clubPostPhotoList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
