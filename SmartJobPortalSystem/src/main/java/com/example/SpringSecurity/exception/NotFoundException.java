package com.example.SpringSecurity.exception;

public class NotFoundException extends RuntimeException {

    private String fieldName;
    private Integer value;

    public NotFoundException(String fieldName, Integer value){
        super(fieldName+" "+value+" not found");
    }

}
