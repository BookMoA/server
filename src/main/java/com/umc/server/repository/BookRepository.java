package com.umc.server.repository;

import com.umc.server.domain.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.id NOT IN :excludedBookIds")
    List<Book> findBooksNotInList(@Param("excludedBookIds") List<Long> excludedBookIds);
}
