package com.umc.server.service.BookListService;

import com.umc.server.domain.BookList;
import java.util.Optional;

public interface BookListQueryService {
    Optional<BookList> getBookList(Long id);
}
