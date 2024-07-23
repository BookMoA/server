package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.AdminRole;
import jakarta.persistence.*;
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
public class AdminMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminRole adminRole;

    @Column(nullable = false, length = 20)
    private String nickName;

    @Column(nullable = false, length = 20)
    private String githubId;

    @Column(nullable = false, length = 100)
    private String emailAddress;

    @Column(length = 20)
    private String snsAddress;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String profileUrl;
}
