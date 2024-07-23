package com.umc.server.domain.mapping;

import com.umc.server.domain.Club;
import com.umc.server.domain.Member;
import com.umc.server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ClubMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean reader;

    @Column(nullable = false, length = 100)
    private String statusMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @OneToOne(mappedBy = "club", cascade = CascadeType.ALL)
    private Member member;
}
