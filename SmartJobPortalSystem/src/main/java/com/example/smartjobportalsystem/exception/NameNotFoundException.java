package com.example.smartjobportalsystem.exception;

public class NameNotFoundException extends RuntimeException{

    public NameNotFoundException(String fieldName, String username){
        super(fieldName+" "+username+" not found");
    }
    public NameNotFoundException(String fieldName){super(fieldName+" not found");}
}
