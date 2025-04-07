package com.flashpay.flashpay.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> fail(String message, T data) {
        return new ApiResponse<>("fail", message, data);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}