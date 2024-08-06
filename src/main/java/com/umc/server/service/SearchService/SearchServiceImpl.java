package com.umc.server.service.SearchService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.SearchHandler;
import com.umc.server.converter.SearchConverter;
import com.umc.server.domain.BookList;
import com.umc.server.domain.BookMemo;
import com.umc.server.repository.BookListRepository;
import com.umc.server.repository.BookMemoRepository;
import com.umc.server.repository.BookRepository;
import com.umc.server.web.dto.response.SearchResponseDTO;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {
    private final BookListRepository bookListRepository;
    private final BookMemoRepository bookMemoRepository;
    private final BookRepository bookRepository;

    public List<SearchResponseDTO.SearchBookListResponseDTO> searchBookList(
            String title, String sortBy, Integer page) {
        // 기본적으로 'relevance' 정렬을 기준으로 설정
        Sort sort;

        switch (sortBy) {
            case "newest":
                sort = Sort.by("createdAt").descending();
                break;
            case "oldest":
                sort = Sort.by("createdAt").ascending();
                break;
            case "rating_desc":
                sort = Sort.by("likeCnt").descending();
                break;
            case "rating_asc":
                sort = Sort.by("likeCnt").ascending();
                break;
            case "relevance":
                sort = Sort.by("title").ascending();
                break;
            default:
                throw new SearchHandler(ErrorStatus.SEARCH_INVALID_SORT);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10, sort);
        List<BookList> bookLists =
                bookListRepository.findByTitleContaining(title, pageRequest).getContent();

        // 검색한 책이 없을 때
        if (bookLists.isEmpty()) {
            throw new SearchHandler(ErrorStatus.SEARCH_BOOKLIST_NOT_FOUND);
        }

        return bookLists.stream()
                .map(SearchConverter::searchBookListResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchResponseDTO.SearchMemoResponseDTO> searchMemoList(
            String keyword, String sortBy, Integer page) {
        Sort sort;
        Long memberId = 1L;

        switch (sortBy) {
            case "newest":
                sort = Sort.by("createdAt").descending();
                break;
            case "oldest":
                sort = Sort.by("createdAt").ascending();
                break;
            case "relevance":
                sort = Sort.by("body").ascending();
                break;
            case "rating_desc":
                sort = Sort.by("memberBook.score").descending();
                break;
            case "rating_asc":
                sort = Sort.by("memberBook.score").ascending();
                break;
            default:
                throw new SearchHandler(ErrorStatus.SEARCH_INVALID_SORT);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10, sort);
        Page<BookMemo> bookMemoPage =
                bookMemoRepository.findByKeywordAndMemberId(keyword, memberId, pageRequest);

        // 검색 결과가 없을 때 예외 처리
        if (bookMemoPage.isEmpty()) {
            throw new SearchHandler(ErrorStatus.SEARCH_BOOKMEMO_NOT_FOUND); // 적절한 예외 처리 필요
        }

        return bookMemoPage.stream()
                .map(SearchConverter::toSearchMemoResponseDTO)
                .collect(Collectors.toList());
    }
}
