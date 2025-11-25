package com.example.SpringSecurity.exception;

public class AlreadyExistsException extends RuntimeException{

    private String fieldName;
    private String value;


    public AlreadyExistsException(String fieldName, String value){
        super(fieldName+" "+value+" already Exists!");
    }
}
