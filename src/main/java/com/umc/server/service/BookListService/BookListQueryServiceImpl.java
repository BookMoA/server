package com.umc.server.service.BookListService;

import com.umc.server.domain.BookList;
import com.umc.server.repository.BookListRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookListQueryServiceImpl implements BookListQueryService {

    private final BookListRepository bookListRepository;

    @Override
    public Optional<BookList> getBookList(Long id) {
        Optional<BookList> bookList = bookListRepository.findById(id);
        return bookList;
    }
}
