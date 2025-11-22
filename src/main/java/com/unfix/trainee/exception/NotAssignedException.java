package com.unfix.trainee.exception;

import com.unfix.trainee.dto.ErrorCode;

public class NotAssignedException extends ApiException {
    public NotAssignedException(String message) {
        super(ErrorCode.NOT_ASSIGNED, message);
    }
}
