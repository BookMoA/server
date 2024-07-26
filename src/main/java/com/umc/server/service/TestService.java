package com.umc.server.service;

import com.umc.server.apiPayload.code.status.ErrorStatus;
import com.umc.server.apiPayload.exception.handler.TestHandler;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public void failedTest() {
        if (1 == 1) {
            throw new TestHandler(ErrorStatus._BAD_REQUEST);
        }
    }
}
