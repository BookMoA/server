package com.umc.server.service.BookMemoService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookMemoHandler;
import com.umc.server.apiPayload.exception.handler.MemberBookHandler;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.BookMemoConverter;
import com.umc.server.domain.BookMemo;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.repository.BookMemoRepository;
import com.umc.server.repository.MemberBookRepository;
import com.umc.server.web.dto.request.BookMemoRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class BookMemoServiceImpl implements BookMemoService {

    private final MemberBookRepository memberBookRepository;
    private final BookMemoRepository bookMemoRepository;

    @Override
    public BookMemo createBookMemo(
            Member member,
            Long memberBookId,
            BookMemoRequestDTO.CreateBookMemoDTO createBookMemoDTO) {
        BookMemo bookMemo = BookMemoConverter.toBookMemo(createBookMemoDTO);

        // 해당 멤버가 독서 메모를 작성할 책을 멤버 책으로 지정했는지 확인
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        bookMemo.setMemberBook(memberBook);

        return bookMemoRepository.save(bookMemo);
    }

    @Override
    @Transactional(readOnly = true)
    public BookMemo readBookMemo(Member member, Long memberBookId, Long bookMemoId) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        // 해당하는 멤버의 멤버 책 찾기
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        // 그 멤버 책의 특정 독서 메모 찾기 (독서 메모 아이디로)
        BookMemo bookMemo =
                bookMemoRepository
                        .findByIdAndMemberBook(bookMemoId, memberBook)
                        .orElseThrow(
                                () -> {
                                    throw new BookMemoHandler(ErrorStatus.BOOK_MEMO_NOT_FOUND);
                                });

        return bookMemo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookMemo> readBookMemoList(Member member, Long memberBookId, Integer page) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        // 해당하는 멤버의 멤버 책 찾기
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        // 그 멤버 책의 모든 독서 메모 찾기
        Page<BookMemo> bookMemoPage =
                bookMemoRepository.findAllByMemberBook(memberBook, PageRequest.of(page, 10));
        return bookMemoPage;
    }

    @Override
    public BookMemo updateBookMemo(
            Member member,
            Long memberBookId,
            Long bookMemoId,
            BookMemoRequestDTO.UpdateBookMemoDTO updateBookMemoDTO) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        // 해당하는 멤버의 멤버 책 찾기
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        // 그 멤버 책의 특정 독서 메모 찾기
        BookMemo bookMemo =
                bookMemoRepository
                        .findByIdAndMemberBook(bookMemoId, memberBook)
                        .orElseThrow(
                                () -> {
                                    throw new BookMemoHandler(ErrorStatus.BOOK_MEMO_NOT_FOUND);
                                });

        bookMemo.setPage(updateBookMemoDTO.getPage());
        bookMemo.setBody(updateBookMemoDTO.getBody());

        return bookMemo;
    }

    @Override
    public void deleteBookMemo(Member member, Long memberBookId, Long bookMemoId) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        // 해당하는 멤버의 멤버 책 찾기
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        // 그 멤버 책의 특정 독서 메모 찾은 후 삭제
        BookMemo bookMemo =
                bookMemoRepository
                        .findByIdAndMemberBook(bookMemoId, memberBook)
                        .orElseThrow(
                                () -> {
                                    throw new BookMemoHandler(ErrorStatus.BOOK_MEMO_NOT_FOUND);
                                });

        bookMemoRepository.delete(bookMemo);
    }
}
