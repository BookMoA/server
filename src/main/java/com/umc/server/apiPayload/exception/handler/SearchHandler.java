package com.umc.server.apiPayload.exception.handler;

import com.umc.server.apiPayload.code.BaseErrorCode;
import com.umc.server.apiPayload.exception.GeneralException;

public class SearchHandler extends GeneralException {
    public SearchHandler(BaseErrorCode code) {
        super(code);
    }
}
