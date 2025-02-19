package com.example.tabletennis.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private Integer code;
    private String message;
    private T result;

    private ApiResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(200,  "API 요청이 성공했습니다.", result);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<Void>(200, "API 요청이 성공했습니다.");
    }

    public static ApiResponse<Void> fail() {
        return new ApiResponse<Void>(201, "API 요청에 실패했습니다.");
    }

    public static ApiResponse<Void> error() {
        return new ApiResponse<Void>(500, "에러가 발생했습니다.");
    }
}
