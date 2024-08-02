package com.umc.server.service.BookService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookHandler;
import com.umc.server.converter.BookConverter;
import com.umc.server.domain.Book;
import com.umc.server.repository.BookRepository;
import com.umc.server.web.dto.BookRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book createBook(BookRequestDTO.CreateBookDTO createBookDTO) {
        Book book = BookConverter.toBook(createBookDTO);
        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Book readBook(Long bookId) {
        return bookRepository
                .findById(bookId)
                .orElseThrow(
                        () -> {
                            throw new BookHandler(ErrorStatus.BOOK_NOT_FOUND);
                        });
    }
}
