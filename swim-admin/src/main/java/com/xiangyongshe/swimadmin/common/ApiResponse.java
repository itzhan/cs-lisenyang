package com.xiangyongshe.swimadmin.common;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.code = 0;
        res.message = "ok";
        res.data = data;
        return res;
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.code = code;
        res.message = message;
        res.data = null;
        return res;
    }
}
