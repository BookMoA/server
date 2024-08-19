package com.umc.server.service.MemberBookService;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.BookHandler;
import com.umc.server.apiPayload.exception.handler.MemberBookHandler;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.MemberBookConverter;
import com.umc.server.domain.Book;
import com.umc.server.domain.BookMemo;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.repository.BookMemoRepository;
import com.umc.server.repository.BookRepository;
import com.umc.server.repository.MemberBookRepository;
import com.umc.server.repository.MemberRepository;
import com.umc.server.web.dto.request.MemberBookRequestDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class MemberBookServiceImpl implements MemberBookService {
    private final MemberBookRepository memberBookRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BookMemoRepository bookMemoRepository;

    @Override
    public MemberBook createMemberBook(
            Member member, MemberBookRequestDTO.CreateMemberBookDTO createMemberBookDTO) {
        MemberBook memberBook = MemberBookConverter.toMemberBook(createMemberBookDTO);
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        Book book =
                bookRepository
                        .findById(createMemberBookDTO.getBookId())
                        .orElseThrow(
                                () -> {
                                    throw new BookHandler(ErrorStatus.BOOK_NOT_FOUND);
                                });

        memberBook.setMember(member);
        memberBook.setBook(book);

        return memberBookRepository.save(memberBook);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberBook readMemberBook(Long memberId, Long memberBookId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        return memberBook;
    }

    @Override
    public MemberBook updateMemberBook(
            Long memberId,
            Long memberBookId,
            MemberBookRequestDTO.UpdateMemberBookDTO updateMemberBookDTO) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        // 새로운 readPage 값이 기존 값보다 작거나 같은 경우 예외 발생
        if (updateMemberBookDTO.getReadPage() <= memberBook.getReadPage()) {
            throw new MemberBookHandler(ErrorStatus.INVALID_READ_PAGE);
        }

        memberBook.setReadPage(updateMemberBookDTO.getReadPage());
        memberBook.setMemberBookStatus(updateMemberBookDTO.getMemberBookStatus());
        memberBook.setEndedAt(updateMemberBookDTO.getEndedAt());
        memberBook.setScore(updateMemberBookDTO.getScore());

        return memberBook;
    }

    @Override
    public void deleteMemberBook(Long memberId, Long memberBookId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
                                });
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        memberBookRepository.delete(memberBook);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberBook readMemberBookByBookMemo(Member member, Long memberBookId) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        // 특정 멤버가 해당 책을 멤버 책으로 가지고 있는지 확인
        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        // 그 멤버 책에 메모가 존재한다면 멤버 책 반환
        Boolean hasBookMemos = bookMemoRepository.existsByMemberBook(memberBook);

        if (hasBookMemos) return memberBook;
        else throw new MemberBookHandler(ErrorStatus.BOOK_MEMO_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberBook> readMemberBookListByBookMemo(Member member, Integer page) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        // 특정 멤버가 가지고 있는 멤버 책들 리스트
        List<MemberBook> memberBookList =
                memberBookRepository
                        .findAllByMember(member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        // 위 리스트에서 메모를 가진 멤버 책만 리스트로 다시 변환
        List<MemberBook> memoMemberBookList =
                bookMemoRepository.findAllByMemberBookIn(memberBookList).stream()
                        .map(BookMemo::getMemberBook) // 각 BookMemo에서 MemberBook을 추출
                        .distinct() // 중복된 MemberBook을 제거
                        .collect(Collectors.toList()); // 리스트로 수집

        // 메모를 가진 멤버 책들이 아예 없을 시 에러 메시지
        if (memoMemberBookList.isEmpty())
            throw new MemberBookHandler(ErrorStatus.BOOK_MEMO_NOT_FOUND);

        // 페이지네이션 적용
        Page<MemberBook> memoMemberBookPage =
                new PageImpl<>(
                        memoMemberBookList,
                        PageRequest.of(page - 1, 15),
                        memoMemberBookList.size());
        return memoMemberBookPage;
    }
}
