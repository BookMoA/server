package com.umc.server.service.BookListService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookListHandler;
import com.umc.server.converter.BookListConverter;
import com.umc.server.converter.MemberBookListConverter;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.domain.mapping.MemberBookList;
import com.umc.server.repository.*;
import com.umc.server.web.dto.request.BookListRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookListServiceImpl implements BookListService {
    private final BookListRepository bookListRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookListEntryRepository bookListEntryRepository;
    private final MemberBookListRepository memberBookListRepository;

    // 책리스트 추가
    @Override
    @Transactional
    public BookList addBookList(BookListRequestDTO.AddBookListDTO request) {
        // 현재 인증된 멤버를 가져오는 부분 (추후 수정)
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // DTO를 엔티티로 변환
        BookList bookList = BookListConverter.toBookList(request, member);

        // BookList 엔티티 저장
        return bookListRepository.save(bookList);
    }

    // 책리스트 수정
    @Override
    public BookList updateBookList(Long bookListId, BookListRequestDTO.UpdateBookListDTO request) {
        // 1. 책 리스트 조회
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        // 2. 상태 값 변환
        ListStatus listStatus;
        try {
            listStatus = ListStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BookListHandler(ErrorStatus.BOOKLIST_INVALID_STATUS); // status값이 올바르지 않으면 오류
        }

        // 3. 책 리스트 정보 업데이트
        bookList.update(request.getTitle(), request.getSpec(), request.getImg(), listStatus);
        // ------------------------------------------------------------------------------------------------------
        // 4. 기존 BookListEntry 삭제
        List<BookListEntry> existingEntries = bookList.getBookListEntry();
        bookListEntryRepository.deleteAll(existingEntries); // 현재 리스트의 모든 BookListEntry 삭제
        bookList.getBookListEntry().clear(); // 현재 리스트에서 모든 BookListEntry 제거

        // 5. 새로 받은 BookListEntry 정보로 업데이트
        List<BookListEntry> newEntries = new ArrayList<>();
        for (BookListRequestDTO.BookListEntryDTO entryDTO : request.getBooks()) {
            Book book =
                    bookRepository
                            .findById(entryDTO.getId())
                            .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOK_NOT_FOUND));

            BookListEntry newEntry =
                    BookListEntry.builder()
                            .book(book)
                            .bookList(bookList)
                            .number(entryDTO.getNumber())
                            .build();

            newEntries.add(newEntry);
            bookList.getBookListEntry().add(newEntry); // 새로운 항목 추가
        }

        // 6. BookList의 bookCnt 업데이트
        bookList.setBookCnt(bookList.getBookListEntry().size());

        // 7. BookList 저장 (bookListEntry도 자동으로 저장됨)
        bookListRepository.save(bookList);

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
                                        () -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND)));
        return bookList;
    }

    // 책리스트 삭제
    @Override
    public void deleteBookList(Long bookListId) {
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));
        bookListRepository.delete(bookList);
    }

    @Override
    public List<BookListResponseDTO.LibraryBookListDTO> getLibraryBookList(Integer page) {
        Long memberId = 1L;
        // PageRequest를 생성하여 페이지네이션 적용
        Page<BookList> bookLists =
                bookListRepository.findStoredBooksByMemberId(1L, PageRequest.of(page - 1, 10));

        // Page<BookList>에서 content만 추출하여 변환
        return bookLists.getContent().stream()
                .map(bookList -> BookListConverter.toLibraryBookListDTO(bookList, memberId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> addBookInBookList(
            Long bookListId, BookListRequestDTO.AddBookInBookListDTO request) {
        // 책 리스트 찾기
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        // 책 추가
        addBooksToBookList(bookList, request.getBooksId());

        return request.getBooksId();
    }

    // 책 리스트의 책 삭제
    @Override
    @Transactional
    public void deleteBookInBookList(
            Long bookListId, BookListRequestDTO.DeleteBookInBookListDTO request) {
        // 책 리스트 찾기
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        // 책 리스트에서 책 찾기
        List<Book> books = bookRepository.findAllById(request.getBooksId());

        if (books.size() != request.getBooksId().size()) {
            throw new BookListHandler(ErrorStatus.BOOK_NOT_FOUND);
        }

        // 기존에 존재하는 책을 확인
        List<Long> existingBookIds =
                bookList.getBookListEntry().stream()
                        .map(entry -> entry.getBook().getId())
                        .collect(Collectors.toList());

        // 해당책 리스트에서 삭제하려는 아이디의 책이 있는지 확인
        List<Long> duplicateBookIds =
                request.getBooksId().stream()
                        .filter(existingBookIds::contains)
                        .collect(Collectors.toList());

        if (duplicateBookIds.isEmpty()) {
            throw new BookListHandler(ErrorStatus.BOOKLIST_BOOK_NO_EXISTS);
        }

        // 삭제할 책 엔티티를 BookListEntry에서 제거
        List<BookListEntry> entriesToRemove =
                bookList.getBookListEntry().stream()
                        .filter(entry -> duplicateBookIds.contains(entry.getBook().getId()))
                        .collect(Collectors.toList());

        // BookListEntry 삭제
        bookList.getBookListEntry().removeAll(entriesToRemove);
        entriesToRemove.forEach(entry -> bookListEntryRepository.delete(entry));

        // 남은 책들의 number를 1부터 순서대로 재정렬
        List<BookListEntry> remainingEntries = bookList.getBookListEntry();
        for (int i = 0; i < remainingEntries.size(); i++) {
            remainingEntries.get(i).setNumber(i + 1);
        }

        // BookList의 bookCnt 값 업데이트
        bookList.setBookCnt(remainingEntries.size());

        // BookList 업데이트
        bookListRepository.save(bookList);
    }

    // 리스트에 책 추가 함수
    private void addBooksToBookList(BookList bookList, List<Long> bookIds) {
        // 책 리스트에서 책 찾기
        List<Book> books = bookRepository.findAllById(bookIds);

        if (books.size() != bookIds.size()) {
            throw new BookListHandler(ErrorStatus.BOOK_NOT_FOUND);
        }

        // 기존에 추가된 책을 확인
        List<Long> existingBookIds =
                bookList.getBookListEntry().stream()
                        .map(entry -> entry.getBook().getId())
                        .collect(Collectors.toList());

        // 새로 추가할 책 리스트에서 기존에 추가된 책이 있는지 확인
        List<Long> duplicateBookIds =
                bookIds.stream().filter(existingBookIds::contains).collect(Collectors.toList());

        if (!duplicateBookIds.isEmpty()) {
            throw new BookListHandler(ErrorStatus.BOOKLIST_BOOK_ALREADY_EXISTS);
        }

        // 새로 추가할 책 리스트에서 기존에 추가되지 않은 책만 선택
        List<Book> newBooks =
                books.stream()
                        .filter(book -> !existingBookIds.contains(book.getId()))
                        .collect(Collectors.toList());

        // 새로 추가할 BookListEntry 생성
        List<BookListEntry> entries = new ArrayList<>();
        int currentBookCount = bookList.getBookListEntry().size();

        for (Book book : newBooks) {
            BookListEntry entry =
                    BookListEntry.builder()
                            .book(book)
                            .bookList(bookList)
                            .number(++currentBookCount) // 현재 개수에 순차적으로 증가시키는 값
                            .build();
            entries.add(entry);
        }

        // BookList에 BookListEntry 추가
        bookList.getBookListEntry().addAll(entries);
        bookList.setBookCnt(currentBookCount); // 책 개수 업데이트

        // BookList 업데이트
        bookListRepository.save(bookList);
    }

    @Override
    public String toggleLike(Long bookListId) {
        Long memberId = 1L;
        int i = 1; // 좋아요 여부
        // 책 리스트와 사용자를 조회
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND));

        MemberBookList memberBookList =
                memberBookListRepository
                        .findByBookListIdAndMemberId(bookListId, memberId)
                        .orElse(null);

        if (memberBookList == null) {
            // 사용자가 좋아요를 누르지 않았으면, 좋아요 추가
            memberBookList =
                    MemberBookListConverter.createMemberBookList(bookList, member, true, false);
            memberBookListRepository.save(memberBookList);

            bookList.setLikeCnt(bookList.getLikeCnt() + 1);
            i = 1;
        } else {
            if (memberBookList.getIsLiked()) {
                // 사용자가 이미 좋아요를 눌렀으면, 좋아요 제거
                memberBookList.setIsLiked(false);
                bookList.setLikeCnt(bookList.getLikeCnt() - 1);
                i = 0;
            } else {
                // 사용자가 좋아요를 누르지 않았으면, 좋아요 추가
                memberBookList.setIsLiked(true);
                bookList.setLikeCnt(bookList.getLikeCnt() + 1);
                i = 1;
            }
        }

        bookListRepository.save(bookList); // 좋아요 개수 저장
        memberBookListRepository.save(memberBookList); // 사용자 좋아요 여부 저장

        if (i == 0) return "좋아요 취소";
        else return "좋아요 추가";
    }
}
