package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* 다음 exception 에 대한 handling 이 필요하다
MethodArgumentNotValidException
RegisterFailureException
 */
@ControllerAdvice
public class RegisterAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handlingException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(e.getParameter().getParameterName() + " 의 유효성 검사 실패!");
    }

    @ExceptionHandler(RegisterFailureException.class)
    public ResponseEntity<String> handlingException(RegisterFailureException e) {
        return ResponseEntity.badRequest().body("회원가입에 실패하였습니다!");
    }
}
