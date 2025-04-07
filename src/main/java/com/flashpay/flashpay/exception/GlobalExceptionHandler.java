package com.flashpay.flashpay.exception;

import com.flashpay.flashpay.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleRuntime(RuntimeException e) {
        return ApiResponse.error("서버 내부 오류가 발생했습니다.");
    }

    // 추후 필요한 CustomException도 여기에 추가 가능
}