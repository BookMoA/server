package com.umc.server.service.BookListService;

import com.umc.server.domain.BookList;
import com.umc.server.web.dto.BookListRequestDTO;
import com.umc.server.web.dto.BookListResponseDTO;
import java.util.List;
import java.util.Optional;

public interface BookListService {

    BookList addBookList(BookListRequestDTO.AddBookListDTO request);

    BookList updateBookList(Long bookListId, BookListRequestDTO.UpdateBookListDTO request);

    Optional<BookList> getBookList(Long id);

    void deleteBookList(Long bookListId);

    List<BookListResponseDTO.LibraryBookListDTO> getLibraryBookList(Integer page);

    List<Long> addBookInBookList(Long bookListId, BookListRequestDTO.AddBookInBookListDTO request);

    void deleteBookInBookList(Long bookListId, BookListRequestDTO.DeleteBookInBookListDTO request);
}
