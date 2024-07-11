package com.project.exception;

public class UserGenericsException extends Exception{
    public UserGenericsException(String message, Exception e) {
        super(message, e);
    }
}
