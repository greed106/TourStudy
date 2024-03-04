package com.ymj.tourstudy.exception;

public class WrongCaptchaException extends RuntimeException{
    public WrongCaptchaException(String message) {
        super(message);
    }
}
