package com.umc.server.service.BookListService;

import com.umc.server.converter.BookListConverter;
import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.repository.BookListRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.BookListRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookListCommandServiceImpl implements BookListCommandService {

    private final BookListRepository bookListRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public BookList addBookList(BookListRequestDTO.AddBookListDTO request) {
        // 현재 인증된 멤버를 가져오는 부분 (예시로서 멤버 ID를 하드코딩 하였습니다)
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new RuntimeException("Member not found"));

        // DTO를 엔티티로 변환
        BookList bookList = BookListConverter.toBookList(request, member);

        // BookList 엔티티 저장
        return bookListRepository.save(bookList);
    }
}
