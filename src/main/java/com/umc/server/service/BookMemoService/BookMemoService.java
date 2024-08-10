package com.umc.server.service.BookMemoService;

import com.umc.server.domain.BookMemo;
import com.umc.server.domain.Member;
import com.umc.server.web.dto.request.BookMemoRequestDTO;
import org.springframework.data.domain.Page;

public interface BookMemoService {
    BookMemo createBookMemo(
            Member member,
            Long memberBookId,
            BookMemoRequestDTO.CreateBookMemoDTO createBookMemoDTO);

    BookMemo readBookMemo(Member signInmember, Long memberBookId, Long bookMemoId);

    Page<BookMemo> readBookMemoList(Member signInmember, Long memberBookId, Integer page);

    BookMemo updateBookMemo(
            Member signInmember,
            Long memberBookId,
            Long bookMemoId,
            BookMemoRequestDTO.UpdateBookMemoDTO updateBookMemoDTO);

    void deleteBookMemo(Member signInmember, Long memberBookId, Long bookMemoId);
}
