package com.umc.server.service.BookService;

import com.umc.server.converter.BookConverter;
import com.umc.server.domain.Book;
import com.umc.server.repository.BookRepository;
import com.umc.server.web.dto.BookRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book createBook(BookRequestDTO.CreateBookDTO createBookDTO) {
        Book book = BookConverter.toBook(createBookDTO);
        return bookRepository.save(book);
    }
}
