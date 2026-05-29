package com.example.smartjobportalsystem.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String fieldName, Integer value){
        super(fieldName+" "+value+" not found");
    }
    public NotFoundException(String message){
        super(message);
    }

}
