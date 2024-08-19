package com.umc.server.service.BookService;

import com.umc.server.domain.Book;
import com.umc.server.web.dto.request.BookRequestDTO;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    Book createBook(BookRequestDTO.CreateBookDTO createBookDTO, MultipartFile imgUrl)
            throws IOException;

    Book readBook(Long bookId);

    Book updateBook(Long bookId, BookRequestDTO.UpdateBookDTO updateBookDTO, MultipartFile imgUrl)
            throws IOException;
}
