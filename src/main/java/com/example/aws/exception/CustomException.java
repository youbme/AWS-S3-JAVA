package com.example.aws.exception;

public class CustomException extends Exception {

    String message;

    CustomException(String str){
        message = str;

    }
}
