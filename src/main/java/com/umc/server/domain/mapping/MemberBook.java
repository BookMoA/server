package com.umc.server.domain.mapping;

import com.umc.server.domain.Book;
import com.umc.server.domain.BookMemo;
import com.umc.server.domain.DailyReading;
import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.MemberBookStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private MemberBookStatus memberBookStatus;

    private Long readPage;

    private LocalDate startedAt;

    private LocalDate endedAt;

    private Integer score;

    @OneToMany(mappedBy = "memberBook", cascade = CascadeType.ALL)
    private List<DailyReading> dailyReadingList = new ArrayList<>();

    @OneToMany(mappedBy = "memberBook", cascade = CascadeType.ALL)
    private List<BookMemo> bookMemoList = new ArrayList<>();

    // mapping
    //    @ManyToOne(fetch = FetchType.정LAZY)
    //    @JoinColumn(name = "member_id")
    //    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    //    public void setMember(Member member) {
    //        this.member = member;
    //    }

    public void setBook(Book book) {
        this.book = book;
    }
}
