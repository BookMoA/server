package com.umc.server.repository;

import com.umc.server.domain.BookList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookListRepository extends JpaRepository<BookList, Long> {}
