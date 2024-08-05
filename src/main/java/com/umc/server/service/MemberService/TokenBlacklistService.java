package com.umc.server.service.MemberService;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new CopyOnWriteArraySet<>();

    public void addToBlacklist(String token) {
        log.info("blacklist 추가됨: ={}", token);
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        log.info("현재 blacklist 존재함: ={}", blacklist.contains(token));
        return blacklist.contains(token);
    }

    public void removeFromBlacklist(String token) {
        blacklist.remove(token);
    }
}
