package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class BookMemoHandler extends GeneralException {
    public BookMemoHandler(BaseErrorCode code) {
        super(code);
    }
}
