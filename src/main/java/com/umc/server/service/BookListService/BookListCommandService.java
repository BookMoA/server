package com.umc.server.service.BookListService;

import com.umc.server.domain.BookList;
import com.umc.server.web.dto.BookListRequestDTO;

public interface BookListCommandService {
    BookList addBookList(BookListRequestDTO.AddBookListDTO request);
}
