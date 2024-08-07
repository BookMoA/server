package com.umc.server.service.BookListService;

import com.umc.server.domain.BookList;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.BookListRequestDTO;
import com.umc.server.web.dto.response.BookListResponseDTO;
import java.util.List;
import java.util.Optional;

public interface BookListService {

    BookList addBookList(BookListRequestDTO.AddBookListDTO request, Member member);

    BookList updateBookList(Long bookListId, BookListRequestDTO.UpdateBookListDTO request);

    Optional<BookList> getBookList(Long id);

    void deleteBookList(Long bookListId);

    List<BookListResponseDTO.LibraryBookListDTO> getLibraryBookList(Integer page, Member member);

    List<Long> addBookInBookList(Long bookListId, BookListRequestDTO.AddBookInBookListDTO request);

    void deleteBookInBookList(Long bookListId, BookListRequestDTO.DeleteBookInBookListDTO request);

    String toggleLike(Long bookListId, Member member);

    BookListResponseDTO.TopBookListAndTimeDTO getTopBookList(Integer page, Member member);

    BookListResponseDTO.AddaAnotherBookListResultDTO anotherToLibrary(
            Long bookListId, Member member);

    void deleteAnotherBookListToLibrary(Long bookListId, Member member);

    BookListResponseDTO.RecommendBookAndTimeDTO getRecommendBooks();
}
