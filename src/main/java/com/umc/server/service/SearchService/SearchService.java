package com.umc.server.service.SearchService;

import com.umc.server.web.dto.response.SearchResponseDTO;
import java.util.List;

public interface SearchService {

    List<SearchResponseDTO.SearchBookListResponseDTO> searchBookList(
            String title, String sortBy, Integer page);

    List<SearchResponseDTO.SearchMemoResponseDTO> searchMemoList(
            String keyword, String sortBy, Integer page);
}
