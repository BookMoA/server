package com.umc.server.service.BookListService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookListHandler;
import com.umc.server.converter.BookConverter;
import com.umc.server.converter.BookListConverter;
import com.umc.server.converter.MemberBookListConverter;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.domain.enums.ListStatus;
import com.umc.server.domain.enums.MemberBookStatus;
import com.umc.server.domain.mapping.BookListEntry;
import com.umc.server.domain.mapping.MemberBookList;
import com.umc.server.repository.*;
import com.umc.server.service.S3Service.S3Service;
import com.umc.server.web.dto.request.BookListRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class BookListServiceImpl implements BookListService {
    private final BookListRepository bookListRepository;
    private final BookRepository bookRepository;
    private final BookListEntryRepository bookListEntryRepository;
    private final MemberBookListRepository memberBookListRepository;
    private final MemberBookRepository memberBookRepository;
    private final S3Service s3Service;
    private List<BookListResponseDTO.RecommendBookDTO> cachedRecommendations = new ArrayList<>();
    private LocalDateTime lastUpdate = LocalDateTime.MIN;

    // 책리스트 추가
    @Override
    @Transactional
    public BookList addBookList(
            BookListRequestDTO.AddBookListDTO request, Member member, MultipartFile img)
            throws IOException {
        // 현재 인증된 멤버를 가져오는 부분
        if (member == null) {
            throw new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND); // 적절한 예외 처리 필요
        }
        String newUrl = null;
        if (img != null && !img.isEmpty()) {
            newUrl = s3Service.uploadFile(img);
        }

        // DTO를 엔티티로 변환
        BookList bookList = BookListConverter.toBookList(request, member, newUrl);

        // BookList 엔티티 저장
        return bookListRepository.save(bookList);
    }

    //    @Override
    //    @Transactional
    //    public BookList addBookList(String title, String spec,String status,Member member, String
    // url) throws IOException{
    //        if (member == null) {
    //            throw new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND); // 적절한 예외 처리 필요
    //        }
    //
    //        // DTO를 엔티티로 변환
    //        BookList bookList = BookListConverter.toBookList(title, spec, status, member, url);
    //
    //        // BookList 엔티티 저장
    //        return bookListRepository.save(bookList);
    //    }

    // 책리스트 수정
    //    @Override
    //    public BookList updateBookList(Long bookListId, BookListRequestDTO.UpdateBookListDTO
    // request) {
    //        // 1. 책 리스트 조회
    //        BookList bookList =
    //                bookListRepository
    //                        .findById(bookListId)
    //                        .orElseThrow(() -> new
    // BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));
    //
    //        // 2. 상태 값 변환
    //        ListStatus listStatus;
    //        try {
    //            listStatus = ListStatus.valueOf(request.getStatus());
    //        } catch (IllegalArgumentException e) {
    //            throw new BookListHandler(ErrorStatus.BOOKLIST_INVALID_STATUS); // status값이 올바르지
    // 않으면 오류
    //        }
    //
    //        // 3. 책 리스트 정보 업데이트
    //        bookList.update(request.getTitle(), request.getSpec(), request.getImg(), listStatus);
    //        //
    // ------------------------------------------------------------------------------------------------------
    //        // 4. 기존 BookListEntry 삭제
    //        List<BookListEntry> existingEntries = bookList.getBookListEntry();
    //        bookListEntryRepository.deleteAll(existingEntries); // 현재 리스트의 모든 BookListEntry 삭제
    //        bookList.getBookListEntry().clear(); // 현재 리스트에서 모든 BookListEntry 제거
    //
    //        // 5. 새로 받은 BookListEntry 정보로 업데이트
    //        List<BookListEntry> newEntries = new ArrayList<>();
    //        for (BookListRequestDTO.BookListEntryDTO entryDTO : request.getBooks()) {
    //            Book book =
    //                    bookRepository
    //                            .findById(entryDTO.getBookId())
    //                            .orElseThrow(() -> new
    // BookListHandler(ErrorStatus.BOOK_NOT_FOUND));
    //
    //            BookListEntry newEntry =
    //                    BookListEntry.builder()
    //                            .book(book)
    //                            .bookList(bookList)
    //                            .number(entryDTO.getNumber())
    //                            .build();
    //
    //            newEntries.add(newEntry);
    //            bookList.getBookListEntry().add(newEntry); // 새로운 항목 추가
    //        }
    //
    //        // 6. BookList의 bookCnt 업데이트
    //        bookList.setBookCnt(bookList.getBookListEntry().size());
    //
    //        // 7. BookList 저장 (bookListEntry도 자동으로 저장됨)
    //        bookListRepository.save(bookList);
    //
    //        return bookList;
    //    }

    // 책리스트 수정
    @Override
    public BookList updateBookList(
            Long bookListId, BookListRequestDTO.UpdateBookListDTO request, MultipartFile img)
            throws IOException {
        // 1.1 책 리스트 조회
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        // 1.2 이미지 불러오기 -> <기존에 이미지 있는데 NULL -> 기존 이미지 / 새 이미지 들어옴 -> 이미지 수정>
        String oldImageUrl = bookList.getImg(); // 기존 이미지 URL

        String newUrl = null;
        if (img != null && !img.isEmpty()) {
            // 새로운 이미지 업로드
            newUrl = s3Service.uploadFile(img);
        } else {
            // 새로운 이미지가 없을 경우 기존 이미지 URL 유지
            newUrl = oldImageUrl;
        }

        // 2. 상태 값 변환
        ListStatus listStatus;
        try {
            listStatus = ListStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BookListHandler(ErrorStatus.BOOKLIST_INVALID_STATUS); // status값이 올바르지 않으면 오류
        }

        // 3. 책 리스트 정보 업데이트
        bookList.update(request.getTitle(), request.getSpec(), newUrl, listStatus);
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
                            .findById(entryDTO.getBookId())
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
    public List<BookListResponseDTO.LibraryBookListDTO> getLibraryBookList(
            Integer page, Member member) {
        Long memberId = member.getId();
        // PageRequest를 생성하여 페이지네이션 적용
        Page<BookList> bookLists =
                bookListRepository.findStoredBooksByMemberId(
                        memberId, PageRequest.of(page - 1, 10));

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
    public String toggleLike(Long bookListId, Member member) {
        Long memberId = member.getId();
        int i = 1; // 좋아요 여부
        // 책 리스트와 사용자를 조회
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        if (member == null) {
            throw new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND); // 적절한 예외 처리 필요
        }

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

    // 인기책리스트 조회
    @Override
    public BookListResponseDTO.TopBookListAndTimeDTO getTopBookList(Integer page, Member member) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "likeCnt"));
        List<BookList> bookLists = bookListRepository.findAll(pageable).getContent();

        LocalDateTime currentDate = LocalDateTime.now();

        Long memberId = member.getId();

        List<BookListResponseDTO.TopBookListDTO> topBookListDTOs =
                bookLists.stream()
                        .map(
                                bookList ->
                                        BookListConverter.topBookListAndTimeDTO(bookList, memberId))
                        .collect(Collectors.toList());

        return BookListResponseDTO.TopBookListAndTimeDTO.builder()
                .updatedAt(currentDate)
                .bookLists(topBookListDTOs)
                .build();
    }

    // 타사용자 책리스트 보관함에 추가
    public BookListResponseDTO.AddaAnotherBookListResultDTO anotherToLibrary(
            Long bookListId, Member member) {
        Long memberId = member.getId();

        // 책 리스트와 사용자를 조회
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        if (member == null) {
            throw new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND); // 적절한 예외 처리 필요
        }

        if (bookList.getMember().getId().equals(memberId)) {
            throw new BookListHandler(ErrorStatus.BOOKLIST_CANNOT_ADD_OWN); // 자신의 리스트는 추가할 수 없음
        }

        MemberBookList memberBookList =
                memberBookListRepository
                        .findByBookListIdAndMemberId(bookListId, memberId)
                        .orElse(null);

        if (memberBookList == null) {
            // 새로운 MemberBookList 엔티티 생성 및 저장
            memberBookList =
                    MemberBookListConverter.createMemberBookList(bookList, member, false, true);
            memberBookListRepository.save(memberBookList);
        } else {
            if (memberBookList.getIsStored()) {
                // 사용자가 이미 리스트에 포함했으면
                throw new BookListHandler(ErrorStatus.BOOKLIST_ALREADY_EXISTS);
            } else {
                // 사용자가 리스트에 포함하지 않았으면, 보관함에 추가
                memberBookList.setIsStored(true);
                memberBookListRepository.save(memberBookList);
            }
        }

        // 결과 DTO 반환
        return BookListResponseDTO.AddaAnotherBookListResultDTO.builder()
                .memberBookId(memberBookList.getId())
                .createdAt(memberBookList.getCreatedAt())
                .build();
    }

    // 타사용자 리스트 삭제!
    public void deleteAnotherBookListToLibrary(Long bookListId, Member member) {
        Long memberId = member.getId();

        // 책 리스트와 사용자를 조회
        BookList bookList =
                bookListRepository
                        .findById(bookListId)
                        .orElseThrow(() -> new BookListHandler(ErrorStatus.BOOKLIST_NOT_FOUND));

        if (member == null) {
            throw new BookListHandler(ErrorStatus.MEMBER_NOT_FOUND); // 적절한 예외 처리 필요
        }

        MemberBookList memberBookList =
                memberBookListRepository
                        .findByBookListIdAndMemberId(bookListId, memberId)
                        .orElse(null);

        if (memberBookList.getIsLiked() == false) {
            memberBookListRepository.delete(memberBookList);
        } else {
            memberBookList.setIsStored(false);
            memberBookListRepository.save(memberBookList);
        }
    }

    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 자정에 실행
    public void updateRecommendations() {
        Pageable topBooksPageable = PageRequest.of(0, 3);
        List<Long> topBookIds = memberBookRepository.findTopBooksByAverageScore(topBooksPageable);

        // 상위 3권의 책 정보를 조회
        List<Book> topBooks = bookRepository.findAllById(topBookIds);

        // 상위 3권을 제외한 나머지 책을 조회
        List<Book> remainingBooks = bookRepository.findBooksNotInList(topBookIds);

        // 랜덤으로 2권 선택
        Collections.shuffle(remainingBooks); // 랜덤으로 섞기
        List<Book> randomBooks =
                remainingBooks.stream()
                        .limit(2) // 상위 2권 선택
                        .collect(Collectors.toList());

        // 추천 DTO 생성
        List<BookListResponseDTO.RecommendBookDTO> recommendBookDTOs = new ArrayList<>();

        // 상위 3권
        recommendBookDTOs.addAll(
                topBooks.stream()
                        .map(BookConverter::toRecommendBookDTO)
                        .collect(Collectors.toList()));

        // 랜덤 2권
        recommendBookDTOs.addAll(
                randomBooks.stream()
                        .map(BookConverter::toRecommendBookDTO)
                        .collect(Collectors.toList()));

        // 캐시 업데이트
        cachedRecommendations = recommendBookDTOs;
        lastUpdate = LocalDateTime.now();

        System.out.println("Recommendations updated at: " + lastUpdate);
    }

    // 책 추천
    public BookListResponseDTO.RecommendBookAndTimeDTO getRecommendBooks() {
        // 다음 업데이트 날짜 (1주일 후)
        LocalDateTime nextUpdate = lastUpdate.plus(1, ChronoUnit.WEEKS);

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 만약 업데이트가 오래된 경우 업데이트를 강제로 호출
        if (cachedRecommendations.isEmpty() || now.isAfter(nextUpdate)) {
            updateRecommendations(); // 데이터가 없거나 너무 오래된 경우 업데이트
        }

        return BookListResponseDTO.RecommendBookAndTimeDTO.builder()
                .updatedAt(lastUpdate)
                .nextUpdate(nextUpdate)
                .books(cachedRecommendations)
                .build();
    }

    // 보관함 책 조회
    public BookListResponseDTO.LibraryBookDTO getLibraryBooks(
            String category, String sortBy, Integer page, Member member) {
        Long memberId = member.getId();

        MemberBookStatus status = null;
        switch (category) {
            case "reading":
                status = MemberBookStatus.READING;
                break;
            case "finished":
                status = MemberBookStatus.FINISHED;
                break;
            case "all":
                status = null;
                break;
            default:
                throw new BookListHandler(ErrorStatus.BOOK_INVALID_CATEGORY);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        Page<Book> booksPage = bookRepository.findBooks(memberId, status, sortBy, pageRequest);

        if (booksPage.isEmpty()) {
            throw new BookListHandler(ErrorStatus.BOOK_NOT_FOUND);
        }

        List<Book> sortedBooks = booksPage.getContent();

        // DTO로 변환
        List<BookListResponseDTO.RecommendBookDTO> books =
                sortedBooks.stream()
                        .map(BookConverter::toRecommendBookDTO)
                        .collect(Collectors.toList());

        return BookListResponseDTO.LibraryBookDTO.builder()
                .bookStatus(category)
                .books(books)
                .build();
    }
}
