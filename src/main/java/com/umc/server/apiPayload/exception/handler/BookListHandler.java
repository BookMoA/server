package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class BookListHandler extends GeneralException {
    public BookListHandler(BaseErrorCode code) {
        super(code);
    }
}
