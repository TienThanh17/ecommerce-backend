package com.bamito.exception;

import com.bamito.dto.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseObject> handleUncategorizedException(Exception ex) {
//        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity.internalServerError().body(ResponseObject.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidation(MethodArgumentNotValidException exception) {

        return ResponseEntity.badRequest().body(ResponseObject.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getFieldError().getDefaultMessage())
                .build());
    }

    @ExceptionHandler(value = CustomizedException.class)
    public ResponseEntity<ResponseObject> handleCustomizedException(CustomizedException exception) {
        int statusCode = exception.getErrorCode().getCode();
        String message = exception.getErrorCode().getMessage();
        var httpStatusCode = exception.getErrorCode().getHttpStatusCode();

        return ResponseEntity.status(httpStatusCode).body(ResponseObject.builder()
                .code(statusCode)
                .message(message)
                .build());
    }
}
