package com.example.smartjobportalsystem.exception;

public class NameNotFoundException extends RuntimeException{

    private String fieldName;
    private String username;

    public NameNotFoundException(String fieldName, String username){
        super(fieldName+" "+username+" not found");
    }
}
