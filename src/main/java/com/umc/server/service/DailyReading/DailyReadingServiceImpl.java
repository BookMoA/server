package com.umc.server.service.DailyReading;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.DailyReadingHandler;
import com.umc.server.apiPayload.exception.handler.MemberBookHandler;
import com.umc.server.apiPayload.exception.handler.MemberHandler;
import com.umc.server.converter.DailyReadingConverter;
import com.umc.server.domain.DailyReading;
import com.umc.server.domain.Member;
import com.umc.server.domain.mapping.MemberBook;
import com.umc.server.repository.DailyReadingRepository;
import com.umc.server.repository.MemberBookRepository;
import com.umc.server.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DailyReadingServiceImpl implements DailyReadingService {

    private final DailyReadingRepository dailyReadingRepository;

    private final MemberBookRepository memberBookRepository;

    private final MemberRepository memberRepository;

    @Override
    public DailyReading createDailyReading(Long memberBookId) {
        MemberBook memberBook =
                memberBookRepository
                        .findById(memberBookId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        DailyReading dailyReading;
        DailyReading latestDailyReading;

        // 처음 읽은 책 하루 독서량 생성 시
        if (dailyReadingRepository.findByMemberBook(memberBook) == null)
            dailyReading = DailyReadingConverter.toDailyReading(memberBook);

        // 이전에 읽은 책 하루 독서량 생성 시
        else {
            latestDailyReading =
                    dailyReadingRepository.findLatestByMemberBook(memberBook).orElseThrow();
            dailyReading =
                    DailyReadingConverter.toDailyReadingAgain(memberBook, latestDailyReading);
        }

        dailyReading.setMemberBook(memberBook);
        return dailyReadingRepository.save(dailyReading);
    }

    @Override
    @Transactional(readOnly = true)
    public DailyReading readDailyReading(Member member, Long memberBookId, LocalDate createdAt) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        MemberBook memberBook =
                memberBookRepository
                        .findByIdAndMember(memberBookId, member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        DailyReading dailyReading =
                dailyReadingRepository
                        .findByMemberBookAndCreatedAt(memberBook, createdAt)
                        .orElseThrow(
                                () -> {
                                    throw new DailyReadingHandler(
                                            ErrorStatus.DAILY_READING_NOT_FOUND);
                                });

        return dailyReading;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyReading> readDailyReadingList(Member member) {
        if (member == null) throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        List<MemberBook> memberBookList =
                memberBookRepository
                        .findAllByMember(member)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });

        // 멤버의 memberBook들을 foreign key로 가지는 dailyReading 모두 리스트로 반환
        List<DailyReading> dailyReadingList =
                dailyReadingRepository.findAllByMemberBookIn(memberBookList);
        return dailyReadingList;
    }

    @Override
    public DailyReading updateDailyReading(Long memberBookId) {
        MemberBook memberBook =
                memberBookRepository
                        .findById(memberBookId)
                        .orElseThrow(
                                () -> {
                                    throw new MemberBookHandler(ErrorStatus.MEMBER_BOOK_NOT_FOUND);
                                });
        DailyReading dailyReading =
                dailyReadingRepository
                        .findLatestByMemberBook(memberBook)
                        .orElseThrow(
                                () -> {
                                    throw new DailyReadingHandler(
                                            ErrorStatus.DAILY_READING_NOT_FOUND);
                                });

        // memberBook update시 set한 값들로 dailyReading 값 수정
        Long updatedDailyRead =
                dailyReading.getDailyRead() + memberBook.getReadPage() - dailyReading.getReadPage();
        dailyReading.setDailyRead(updatedDailyRead);
        dailyReading.setReadPage(memberBook.getReadPage());
        return dailyReading;
    }
}
