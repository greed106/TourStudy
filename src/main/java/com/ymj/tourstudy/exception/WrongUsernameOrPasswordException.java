package com.ymj.tourstudy.exception;

public class WrongUsernameOrPasswordException extends RuntimeException{
    public WrongUsernameOrPasswordException(String message) {
        super(message);
    }

}
