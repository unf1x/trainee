package com.unfix.trainee.dto;

public record ErrorResponse(
        ErrorBody error
) {
    public record ErrorBody(
            ErrorCode code,
            String message
    ) {}
}
