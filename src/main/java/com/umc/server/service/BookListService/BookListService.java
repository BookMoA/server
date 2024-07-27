package com.umc.server.service.BookListService;

import com.umc.server.domain.BookList;
import com.umc.server.web.dto.BookListRequestDTO;
import java.util.Optional;

public interface BookListService {

    BookList addBookList(BookListRequestDTO.AddBookListDTO request);

    BookList updateBookList(Long bookListId, BookListRequestDTO.UpdateBookListDTO request);

    Optional<BookList> getBookList(Long id);

    void deleteBookList(Long bookListId);
}
