package com.example.SpringSecurity.exception;


import com.example.SpringSecurity.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.NameNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionalHandler {



    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleAlreadyExistsException(AlreadyExistsException exception){

        ApiResponse response=new ApiResponse(LocalDateTime.now(),"ALREADY_EXISTS",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleAlreadyExistsException(NotFoundException exception){

        ApiResponse response=new ApiResponse(LocalDateTime.now(),"ID_NOT_FOUND",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ApiResponse> handleAlreadyExistsException(UnAuthorizedException exception){

        ApiResponse response=new ApiResponse(LocalDateTime.now(),"Unauthorized",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(NameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNameNotFoundException(NameNotFoundException exception){

        ApiResponse response=new ApiResponse(LocalDateTime.now(),"USER_NAME_NOTFOUND",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
