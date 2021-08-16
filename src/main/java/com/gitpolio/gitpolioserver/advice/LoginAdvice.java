package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.advice.error.ErrorResponse;
import com.gitpolio.gitpolioserver.advice.error.ErrorStatus;
import com.gitpolio.gitpolioserver.exception.LoginFailureException;
import com.gitpolio.gitpolioserver.exception.UnknownIdTypeException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.Valid;

/* 다음 exception 에 대한 handling 이 필요하다
UnknownIdTypeException
LoginFailureException
 */@ControllerAdvice
public class LoginAdvice {
    @ExceptionHandler(UnknownIdTypeException.class)
    public ResponseEntity<ErrorResponse> handleException(UnknownIdTypeException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message("아이디 Type 을 찾을 수 없습니다!")
                .status(ErrorStatus.LOGIN_ERROR)
                .build();
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<ErrorResponse> handleException(LoginFailureException e) {
        ErrorResponse response;
        switch (e.getReason()) {
            case ID_NOT_FOUND:
                response = ErrorResponse.builder()
                        .message("아이디를 찾을 수 없습니다!")
                        .status(ErrorStatus.LOGIN_ID_NOT_FOUND)
                        .build();
                return  ResponseEntity.badRequest().body(response);
            case WRONG_PASSWORD:
                response = ErrorResponse.builder()
                        .message("잘못된 비밀번호입니다!")
                        .status(ErrorStatus.LOGIN_WRONG_PASSWORD)
                        .build();
                return ResponseEntity.badRequest().body(response);
            default:
                response = ErrorResponse.builder()
                        .message("로그인중 오류가 발생하였습니다!!")
                        .status(ErrorStatus.LOGIN_ERROR)
                        .build();
                return ResponseEntity.internalServerError().body(response);
        }
    }


}
