package com.umc.server.domain.mapping;

import com.umc.server.domain.ClubPost;
import com.umc.server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ClubPostComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_post_id", nullable = false)
    private ClubPost clubPost;

    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "member_id", nullable = false)
    //    private Member member;
}
