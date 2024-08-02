package com.umc.server.service.BookListService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.GeneralException;
import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.repository.BookEntryRepository;
import com.umc.server.repository.BookListEntryRepository;
import com.umc.server.repository.BookListRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.BookListRequestDTO;
import com.umc.server.web.dto.BookListResponseDTO;
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
    private final BookEntryRepository bookRepository;
    private final BookListEntryRepository bookListEntryRepository;

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

    @Override
    public List<BookListResponseDTO.LibraryBookListDTO> getLibraryBookList(Integer page) {
        // PageRequest를 생성하여 페이지네이션 적용
        Page<BookList> bookLists =
                bookListRepository.findStoredBooksByMemberId(1L, PageRequest.of(page, 10));

        // Page<BookList>에서 content만 추출하여 변환
        return bookLists.getContent().stream()
                .map(BookListConverter::toLibraryBookListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> addBookInBookList(
            Long bookListId, BookListRequestDTO.AddBookInBookListDTO request) {
        // 책 리스트 찾기
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKLIST_NOT_FOUND));

        // 책 리스트에서 책 찾기
        List<Book> books = bookRepository.findAllById(request.getBooksId());

        if (books.size() != request.getBooksId().size()) {
            throw new GeneralException(ErrorStatus.BOOK_NOT_FOUND);
        }

        // 기존에 추가된 책을 확인
        List<Long> existingBookIds =
                bookList.getBookListEntry().stream()
                        .map(entry -> entry.getBook().getId())
                        .collect(Collectors.toList());

        // 새로 추가할 책 리스트에서 기존에 추가된 책이 있는지 확인
        List<Long> duplicateBookIds =
                request.getBooksId().stream()
                        .filter(existingBookIds::contains)
                        .collect(Collectors.toList());

        if (!duplicateBookIds.isEmpty()) {
            throw new GeneralException(ErrorStatus.BOOKLIST_BOOK_ALREADY_EXISTS);
        }

        // 새로 추가할 책 리스트에서 기존에 추가되지 않은 책만 선택
        List<Book> newBooks =
                books.stream()
                        .filter(book -> !existingBookIds.contains(book.getId()))
                        .collect(Collectors.toList());

        // 새로 추가할 BookListEntry 생성
        List<BookListEntry> entries = new ArrayList<>();
        // 기존에 추가된 책의 개수 확인
        int currentBookCount = bookList.getBookListEntry().size();

        // BookListEntry객체 생성
        for (Book book : newBooks) {
            BookListEntry entry =
                    BookListEntry.builder()
                            .book(book)
                            .bookList(bookList)
                            .number(++currentBookCount) // 현재 개수에 순차적으로 증가시키는 값
                            .build();
            entries.add(bookListEntryRepository.save(entry));
        }

        // BookList에 BookListEntry 추가
        bookList.getBookListEntry().addAll(entries);
        bookList.setBookCnt(currentBookCount); // 책개수 추가

        // BookList 업데이트
        bookListRepository.save(bookList);

        return request.getBooksId();
    }

    @Override
    @Transactional
    public void deleteBookInBookList(
            Long bookListId, BookListRequestDTO.DeleteBookInBookListDTO request) {
        // 책 리스트 찾기
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKLIST_NOT_FOUND));

        // 책 리스트에서 책 찾기
        List<Book> books = bookRepository.findAllById(request.getBooksId());

        if (books.size() != request.getBooksId().size()) {
            throw new GeneralException(ErrorStatus.BOOK_NOT_FOUND);
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
            throw new GeneralException(ErrorStatus.BOOKLIST_BOOK_NO_EXISTS);
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
}
