package com.umc.server.service.MemberService;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklist = new CopyOnWriteArraySet<>();

    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public void removeFromBlacklist(String token) {
        blacklist.remove(token);
    }
}
