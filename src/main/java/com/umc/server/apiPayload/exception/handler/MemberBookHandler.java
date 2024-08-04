package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class MemberBookHandler extends GeneralException {
    public MemberBookHandler(BaseErrorCode code) {
        super(code);
    }
}
