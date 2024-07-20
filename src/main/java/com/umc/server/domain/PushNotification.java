package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자를 생성하지만 접근 수준을 protected로 제한
@DynamicUpdate
@DynamicInsert
public class PushNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean likePushEnabled;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean commentPushEnabled;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean nightPushEnabled;

    @OneToOne()
    @JoinColumn(name = "member_id")
    private Member member;
}
