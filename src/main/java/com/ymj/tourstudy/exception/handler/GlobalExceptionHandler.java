package com.ymj.tourstudy.exception.handler;


import com.ymj.tourstudy.exception.DuplicateUsernameException;
import com.ymj.tourstudy.exception.NotLoggedInException;
import com.ymj.tourstudy.exception.WrongUsernameOrPasswordException;
import com.ymj.tourstudy.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(NotLoggedInException.class)
//    public Result handleNotLoggedInException(NotLoggedInException ex){
//
//        return Result.error(ex.getMessage());
//    }
//
//    @ExceptionHandler(WrongUsernameOrPasswordException.class)
//    public Result handleWrongInformationException(NotLoggedInException ex){
//        return Result.error(ex.getMessage());
//    }
//
//    @ExceptionHandler(DuplicateUsernameException.class)
//    public Result handleDuplicateUsernameException(DuplicateUsernameException ex){
//        return Result.error(ex.getMessage());
//    }
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex){
        return Result.error(ex.getMessage());
    }
}
