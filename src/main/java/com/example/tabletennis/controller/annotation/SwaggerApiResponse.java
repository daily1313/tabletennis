package com.example.tabletennis.controller.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation()
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API 요청이 성공했습니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "201", description = "불가능한 요청입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "에러가 발생했습니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiResponse.class)))
        })
public @interface SwaggerApiResponse {

    String summary() default "";

    Class<?> implementation() default Void.class;
}
