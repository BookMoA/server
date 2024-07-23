package com.umc.server.domain;

import com.umc.server.domain.common.BaseEntity;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.domain.mapping.MemberBookList;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
public class BookList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String img;

    @Column(nullable = false, length = 300)
    private String spec;

    private Integer like;

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
}
