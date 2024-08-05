package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.domain.mapping.MemberBookList;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@Builder
public class BookList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String img;

    @Column(nullable = false, length = 52)
    private String spec;

    @ColumnDefault("0")
    private Integer likeCnt;

    @ColumnDefault("0")
    private Integer bookCnt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListStatus listStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "bookList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberBookList> memberBookList = new ArrayList<>();

    @OneToMany(mappedBy = "bookList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BookListEntry> bookListEntry = new ArrayList<>();

    public void update(String title, String spec, String img, ListStatus listStatus) {
        this.title = title;
        this.spec = spec;
        this.img = img;
        this.listStatus = listStatus;
    }

    public void setBookCnt(int i) {
        this.bookCnt = i;
    }

    public void setLikeCnt(int i) {
        this.likeCnt = i;
    }
}
