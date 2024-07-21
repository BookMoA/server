package com.umc.server.domain;

import com.umc.server.common.BaseEntity;
import com.umc.server.mapping.MemberBook;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BookMemo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long page;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String body;

    // mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_book_id")
    private MemberBook memberBook;

    public void setMemberBook(MemberBook memberBook) {
        this.memberBook = memberBook;
    }
}
