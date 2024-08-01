package com.umc.server.repository;

import com.umc.server.domain.mapping.BookListEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookListEntryRepository extends JpaRepository<BookListEntry, Long> {}
