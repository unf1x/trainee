package com.unfix.trainee.exception;

import com.unfix.trainee.dto.ErrorCode;

public class TeamExistsException extends ApiException {
    public TeamExistsException(String message) {
        super(ErrorCode.TEAM_EXISTS, message);
    }
}
