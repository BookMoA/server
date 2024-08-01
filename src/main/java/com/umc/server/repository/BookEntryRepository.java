package com.umc.server.repository;

import com.umc.server.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEntryRepository extends JpaRepository<Book, Long> {}
