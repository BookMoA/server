package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.AdminRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 생성하지만 접근 수준을 protected로 제한
@DynamicUpdate
@DynamicInsert
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

    @Column(nullable = false, length = 20)
    private String emailAddress;

    private String snsAddress;
    private String profileUrl;
}
