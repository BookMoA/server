package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class DailyReadingHandler extends GeneralException {
    public DailyReadingHandler(BaseErrorCode code) {
        super(code);
    }
}
