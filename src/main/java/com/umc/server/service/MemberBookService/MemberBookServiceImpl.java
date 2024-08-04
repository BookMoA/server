package com.umc.server.service.MemberBookService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookHandler;
import com.umc.server.apiPayload.exception.handler.MemberBookHandler;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberBookConverter;
import com.umc.server.domain.Book;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.repository.BookRepository;
import com.umc.server.repository.MemberBookRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.request.MemberBookRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class MemberBookServiceImpl implements MemberBookService {
    private final MemberBookRepository memberBookRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberBook createMemberBook(
            Long memberId, MemberBookRequestDTO.CreateMemberBookDTO createMemberBookDTO) {
        MemberBook memberBook = MemberBookConverter.toMemberBook(createMemberBookDTO);
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        Book book =
                bookRepository
                        .findById(createMemberBookDTO.getBookId())
                        .orElseThrow(
                                () -> {
                                    throw new BookHandler(ErrorStatus.BOOK_NOT_FOUND);
                                });

        memberBook.setBook(book);
        memberBook.setMember(member);

        return memberBookRepository.save(memberBook);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberBook readMemberBook(Long memberId, Long memberBookId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        return memberBook;
    }

    @Override
    public MemberBook updateMemberBook(
            Long memberId,
            Long memberBookId,
            MemberBookRequestDTO.UpdateMemberBookDTO updateMemberBookDTO) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        memberBook.setReadPage(updateMemberBookDTO.getReadPage());
        memberBook.setMemberBookStatus(updateMemberBookDTO.getMemberBookStatus());
        memberBook.setEndedAt(updateMemberBookDTO.getEndedAt());
        memberBook.setScore(updateMemberBookDTO.getScore());
        return memberBook;
    }

    @Override
    public void deleteMemberBook(Long memberId, Long memberBookId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        memberBookRepository.delete(memberBook);
    }
}
