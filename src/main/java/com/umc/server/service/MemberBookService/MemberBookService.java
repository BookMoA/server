package com.umc.server.service.MemberBookService;

import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.web.dto.request.MemberBookRequestDTO;

public interface MemberBookService {
    MemberBook createMemberBook(
            Member member, MemberBookRequestDTO.CreateMemberBookDTO createMemberBookDTO);

    MemberBook readMemberBook(Long memberId, Long memberBookId);

    MemberBook updateMemberBook(
            Long memberId,
            Long memberBookId,
            MemberBookRequestDTO.UpdateMemberBookDTO updateMemberBookDTO);

    void deleteMemberBook(Long memberId, Long memberBookId);

    MemberBook readMemberBookByBookMemo(Member signInmember, Long memberBookId);
}
