package com.unfix.trainee.exception;

import com.unfix.trainee.dto.ErrorCode;

public class PrExistsException extends ApiException {
    public PrExistsException(String message) {
        super(ErrorCode.PR_EXISTS, message);
    }
}
