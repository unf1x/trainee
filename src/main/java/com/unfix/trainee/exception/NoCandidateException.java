package com.unfix.trainee.exception;

import com.unfix.trainee.dto.ErrorCode;

public class NoCandidateException extends ApiException {
    public NoCandidateException(String message) {
        super(ErrorCode.NO_CANDIDATE, message);
    }
}
