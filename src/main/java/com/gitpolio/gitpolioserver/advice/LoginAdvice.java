package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.exception.LoginFailureException;
import com.gitpolio.gitpolioserver.exception.UnknownIdTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* 다음 exception 에 대한 handling 이 필요하다
UnknownIdTypeException
LoginFailureException
 */@ControllerAdvice
public class LoginAdvice {
    @ExceptionHandler(UnknownIdTypeException.class)
    public ResponseEntity<String> handleException(UnknownIdTypeException e) {
        return ResponseEntity.internalServerError().body("알 수 없는 IdType 입니다! (LoginInfo 분석 실패)");
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerException(LoginFailureException e) {
        return ResponseEntity.badRequest().body("로그인에 실패하였습니다!");
    }
}
