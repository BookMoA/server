package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class TestHandler extends GeneralException {
    public TestHandler(BaseErrorCode code) {
        super(code);
    }
}