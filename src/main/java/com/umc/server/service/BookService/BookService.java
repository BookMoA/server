package com.umc.server.service.BookService;

import com.umc.server.domain.Book;
import com.umc.server.web.dto.BookRequestDTO;

public interface BookService {
    Book createBook(BookRequestDTO.CreateBookDTO createBookDTO);

    Book readBook(Long bookId);
}
