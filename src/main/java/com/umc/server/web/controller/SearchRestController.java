package com.umc.server.web.controller;

import com.umc.server.apiPayload.ApiResponse;
import com.umc.server.service.SearchService.SearchService;
import com.umc.server.web.dto.response.SearchResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchRestController {
    private final SearchService searchService;

    @Operation(
            summary = "책리스트 검색 API",
            description =
                    "제목에 책리스트를 검색하는 API입니다. sortBy로 정렬 가능합니다. ['newest' - 최신순, 'oldest' - 오래된 순, 'relevance' - 정확도 순, 'rating_desc' - 별점 낮은 순, 'rating_asc' - 별점 높은 순]")
    @GetMapping("/list")
    public ApiResponse<List<SearchResponseDTO.SearchBookListResponseDTO>> searchBookList(
            @RequestParam(name = "title") @Parameter(description = "검색할 책리스트의 제목") String title,
            @RequestParam(name = "sortBy", defaultValue = "relevance")
                    @Parameter(description = "정렬 기준")
                    String sortBy,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {

        List<SearchResponseDTO.SearchBookListResponseDTO> bookList =
                searchService.searchBookList(title, sortBy, page);
        return ApiResponse.onSuccess(bookList);
    }
}
