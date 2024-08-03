package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
