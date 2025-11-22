package com.unfix.trainee.exception;

import com.unfix.trainee.dto.ErrorCode;
import com.unfix.trainee.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        ErrorResponse body = new ErrorResponse(
                new ErrorResponse.ErrorBody(ErrorCode.NOT_FOUND, ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(TeamExistsException.class)
    public ResponseEntity<ErrorResponse> handleTeamExists(TeamExistsException ex) {
        ErrorResponse body = new ErrorResponse(
                new ErrorResponse.ErrorBody(ErrorCode.TEAM_EXISTS, ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        if (ex.getCode() == ErrorCode.NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }
        ErrorResponse body = new ErrorResponse(
                new ErrorResponse.ErrorBody(ex.getCode(), ex.getMessage())
        );
        return ResponseEntity.status(status).body(body);
    }
    @ExceptionHandler(PrExistsException.class)
    public ResponseEntity<ErrorResponse> handlePrExists(PrExistsException ex) {
        ErrorResponse body = new ErrorResponse(
                new ErrorResponse.ErrorBody(ErrorCode.PR_EXISTS, ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

}
