package com.example.smartjobportalsystem.exception;


import com.example.smartjobportalsystem.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.NameNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionalHandler {



    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO> handleAlreadyExistsException(AlreadyExistsException exception){

        ApiResponseDTO response=new ApiResponseDTO(LocalDateTime.now(),"ALREADY_EXISTS",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleAlreadyExistsException(NotFoundException exception){

        ApiResponseDTO response=new ApiResponseDTO(LocalDateTime.now(),"ID_NOT_FOUND",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ApiResponseDTO> handleAlreadyExistsException(UnAuthorizedException exception){

        ApiResponseDTO response=new ApiResponseDTO(LocalDateTime.now(),"Unauthorized",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(NameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleNameNotFoundException(NameNotFoundException exception){

        ApiResponseDTO response=new ApiResponseDTO(LocalDateTime.now(),"USER_NAME_NOTFOUND",exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidLoginRequestException.class)
    public ResponseEntity<ApiResponseDTO> handleInvalidLoginRequestException(InvalidLoginRequestException exception){
        ApiResponseDTO apiResponse =new ApiResponseDTO(LocalDateTime.now(),"Invalid Login request Payload",exception.getMessage());
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }


}
