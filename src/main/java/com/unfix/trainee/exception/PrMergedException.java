package com.unfix.trainee.exception;

import com.unfix.trainee.dto.ErrorCode;

public class PrMergedException extends ApiException {
    public PrMergedException(String message) {
        super(ErrorCode.PR_MERGED, message);
    }
}
