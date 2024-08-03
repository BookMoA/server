package com.umc.server.service.SearchService;

import com.umc.server.converter.SearchConverter;
import com.umc.server.domain.BookList;
import com.umc.server.repository.BookListRepository;
import com.umc.server.web.dto.response.SearchResponseDTO;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {
    private final BookListRepository bookListRepository;

    public List<SearchResponseDTO.SearchBookListResponseDTO> searchBookList(
            String title, String sortBy, Integer page) {
        // 기본적으로 'relevance' 정렬을 기준으로 설정
        Sort sort = Sort.by("title").ascending();

        switch (sortBy) {
            case "newest":
                sort = Sort.by("createdAt").descending();
                break;
            case "oldest":
                sort = Sort.by("createdAt").ascending();
                break;
            case "rating_desc":
                sort = Sort.by("likeCnt").ascending();
                break;
            case "rating_asc":
                sort = Sort.by("likeCnt").descending();
                break;
            default:
                // 기본값은 'relevance'로 처리
                break;
        }

        PageRequest pageRequest = PageRequest.of(page, 10, sort);
        List<BookList> bookLists =
                bookListRepository.findByTitleContaining(title, pageRequest).getContent();
        // listStatus가 "PUBLIC"인 항목만 필터링
        List<BookList> filteredBookLists =
                bookLists.stream()
                        .filter(bookList -> "PUBLIC".equals(bookList.getListStatus().name()))
                        .collect(Collectors.toList());

        return filteredBookLists.stream()
                .map(SearchConverter::searchBookListResponseDTO)
                .collect(Collectors.toList());
    }
}
