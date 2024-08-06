package com.umc.server.converter;

import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBookList;

public class MemberBookListConverter {

    public static MemberBookList createMemberBookList(
            BookList bookList, Member member, boolean isLiked, boolean isStored) {
        return MemberBookList.builder()
                .bookList(bookList)
                .member(member)
                .isLiked(isLiked)
                .isStored(isStored)
                .build();
    }
}
