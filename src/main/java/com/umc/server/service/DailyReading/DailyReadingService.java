package com.umc.server.service.DailyReading;

import com.umc.server.domain.DailyReading;
import com.umc.server.domain.Member;
import java.time.LocalDate;
import java.util.List;

public interface DailyReadingService {
    DailyReading createDailyReading(Long memberBookId);

    DailyReading readDailyReading(Member signInmember, Long memberBookId, LocalDate createdAt);

    List<DailyReading> readDailyReadingList(Member signInmember);

    DailyReading updateDailyReading(Long memberBookId);
}
