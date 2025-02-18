package com.example.tabletennis.handler;

import com.example.tabletennis.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleException(RuntimeException e) {
        return ApiResponse.fail();
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleInternalServerException(Exception e) {
        return ApiResponse.error();
    }
}
