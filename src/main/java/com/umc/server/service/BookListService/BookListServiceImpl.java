package com.umc.server.service.BookListService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.repository.BookListRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.BookListRequestDTO;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookListServiceImpl implements BookListService {
    private final BookListRepository bookListRepository;
    private final MemberRepository memberRepository;

    // 책리스트 추가
    @Override
    @Transactional
    public BookList addBookList(BookListRequestDTO.AddBookListDTO request) {
        // 현재 인증된 멤버를 가져오는 부분 (추후 수정)
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // DTO를 엔티티로 변환
        BookList bookList = BookListConverter.toBookList(request, member);

        // BookList 엔티티 저장
        return bookListRepository.save(bookList);
    }

    // 책리스트 수정
    @Override
    public BookList updateBookList(Long bookListId, BookListRequestDTO.UpdateBookListDTO request) {
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKLIST_NOT_FOUND));

        ListStatus listStatus;
        try {
            listStatus = ListStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new GeneralException(
                    ErrorStatus.BOOKLIST_INVALID_STATUS); // status값이 PUBLIC이나 PRIVATE이 아니라면 ERRROR
        }

        bookList.update(request.getTitle(), request.getSpec(), request.getImg(), listStatus);
        return bookList;
    }

    // 책리스트 조회
    @Override
    public Optional<BookList> getBookList(Long id) {
        Optional<BookList> bookList =
                Optional.ofNullable(
                        bookListRepository
                                .findById(id)
                                .orElseThrow(
                                        () ->
                                                new GeneralException(
                                                        ErrorStatus.BOOKLIST_NOT_FOUND)));
        return bookList;
    }

    // 책리스트 삭제 d
    @Override
    public void deleteBookList(Long bookListId) {
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKLIST_NOT_FOUND));
        bookListRepository.delete(bookList);
    }
}
