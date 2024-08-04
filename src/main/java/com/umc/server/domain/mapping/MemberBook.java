package com.umc.server.domain.mapping;

import com.umc.server.domain.Book;
import com.umc.server.domain.BookMemo;
import com.umc.server.domain.DailyReading;
import com.umc.server.domain.Member;
import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.MemberBookStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@Builder
public class MemberBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private MemberBookStatus memberBookStatus;

    private Long readPage;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate startedAt;

    private LocalDate endedAt;

    private Integer score;

    @OneToMany(mappedBy = "memberBook", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DailyReading> dailyReadingList = new ArrayList<>();

    @OneToMany(mappedBy = "memberBook", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BookMemo> bookMemoList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
