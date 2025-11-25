package com.example.SpringSecurity.exception;

public class UnAuthorizedException extends RuntimeException{


    private String fieldName;
    private String value;

    public UnAuthorizedException(String fieldName, String value){
        super(fieldName+" "+value+" is UnAuthorized");
    }





}
