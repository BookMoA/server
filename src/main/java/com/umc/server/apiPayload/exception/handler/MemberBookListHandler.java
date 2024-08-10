package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class MemberBookListHandler extends GeneralException {
    public MemberBookListHandler(BaseErrorCode code) {
        super(code);
    }
}
