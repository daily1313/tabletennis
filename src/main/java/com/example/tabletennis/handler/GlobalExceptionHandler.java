package com.example.tabletennis.handler;

import com.example.tabletennis.dto.response.ApiResponse;
import com.example.tabletennis.exception.room.*;
import com.example.tabletennis.exception.user.*;
import com.example.tabletennis.exception.userroom.TeamFullException;
import com.example.tabletennis.exception.userroom.UserRoomNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CannotExitRoomException.class, RoomFullException.class, RoomInNotWaitStateException.class
                      , RoomNotFoundException.class, UserAlreadyInRoomException.class, UserNotActiveException.class
                      , UserNotInRoomException.class, TeamFullException.class, UserNotFoundException.class
                      , UserNotHostException.class, RoomNotFullException.class, RoomWaitStateException.class
                      , UserRoomNotFoundException.class, RoomNotProgressStateException.class, RestApiRequestFailureException.class})
    public ApiResponse<Void> handleException(RuntimeException e) {
        return ApiResponse.fail();
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleInternalServerException(Exception e) {
        return ApiResponse.error();
    }
}
