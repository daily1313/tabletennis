package com.example.tabletennis.handler;

import com.example.tabletennis.common.dto.ApiResponse;
import com.example.tabletennis.exception.room.CannotExitRoomException;
import com.example.tabletennis.exception.room.RoomFullException;
import com.example.tabletennis.exception.room.RoomInWaitStateException;
import com.example.tabletennis.exception.room.RoomNotFoundException;
import com.example.tabletennis.exception.user.UserAlreadyInRoomException;
import com.example.tabletennis.exception.user.UserNotActiveException;
import com.example.tabletennis.exception.user.UserNotInRoomException;
import com.example.tabletennis.exception.userroom.TeamFullException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CannotExitRoomException.class, RoomFullException.class, RoomInWaitStateException.class
                      , RoomNotFoundException.class, UserAlreadyInRoomException.class, UserNotActiveException.class
                      , UserNotInRoomException.class, TeamFullException.class})
    public ApiResponse<Void> handleException(RuntimeException e) {
        return ApiResponse.fail();
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleInternalServerException(Exception e) {
        return ApiResponse.error();
    }
}
