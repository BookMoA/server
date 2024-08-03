package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class BookHandler extends GeneralException {
    public BookHandler(BaseErrorCode code) {
        super(code);
    }
}
