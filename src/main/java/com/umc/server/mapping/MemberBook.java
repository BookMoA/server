package com.umc.server.mapping;

import com.umc.server.common.BaseEntity;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookMemo;
import com.umc.server.domain.DailyReading;
import com.umc.server.enums.MemberBookStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "dailyReading", cascade = CascadeType.ALL)
    private List<DailyReading> dailyReadingList = new ArrayList<>();

    @OneToMany(mappedBy = "bookMemo", cascade = CascadeType.ALL)
    private List<BookMemo> bookMemoList = new ArrayList<>();

    // mapping
//    @ManyToOne(fetch = FetchType.LAZY)
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

