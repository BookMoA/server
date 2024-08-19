package com.umc.server.service.BookService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookHandler;
import com.umc.server.converter.BookConverter;
import com.umc.server.domain.Book;
import com.umc.server.repository.BookRepository;
import com.umc.server.service.S3Service.S3Service;
import com.umc.server.web.dto.request.BookRequestDTO;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final S3Service s3Service;

    @Override
    public Book createBook(BookRequestDTO.CreateBookDTO createBookDTO, MultipartFile imgUrl)
            throws IOException {
        String setUrl;

        try {
            setUrl = s3Service.uploadFile(imgUrl);
        } catch (Exception e) {
            throw new IOException("이미지 업로드 중 오류가 발생하였습니다.", e);
        }

        Book book = BookConverter.toBook(createBookDTO, setUrl);
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

    @Override
    public Book updateBook(
            Long bookId, BookRequestDTO.UpdateBookDTO updateBookDTO, MultipartFile imgUrl)
            throws IOException {
        Book book =
                bookRepository
                        .findById(bookId)
                        .orElseThrow(
                                () -> {
                                    throw new BookHandler(ErrorStatus.BOOK_NOT_FOUND);
                                });

        String oldImageUrl = book.getCoverImage();

        String newUrl = null;
        if (imgUrl != null && !imgUrl.isEmpty()) {
            newUrl = s3Service.uploadFile(imgUrl);
        } else {
            newUrl = oldImageUrl;
        }

        book.setTitle(updateBookDTO.getTitle());
        book.setWriter(updateBookDTO.getWriter());
        book.setDescription(updateBookDTO.getDescription());
        book.setPublisher(updateBookDTO.getPublisher());
        book.setIsbn(updateBookDTO.getIsbn());
        book.setPage(updateBookDTO.getPage());
        book.setCoverImage(newUrl);
        return book;
    }
}
