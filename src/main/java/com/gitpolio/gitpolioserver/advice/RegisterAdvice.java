package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.advice.error.ErrorResponse;
import com.gitpolio.gitpolioserver.advice.error.ErrorStatus;
import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

/* 다음 exception 에 대한 handling 이 필요하다
MethodArgumentNotValidException
RegisterFailureException
 */
@ControllerAdvice
public class RegisterAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message("회원가입 정보 검증에 실패하였습니다!")
                .status(ErrorStatus.REGISTER_WRONG_REGISTER_INFO)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RegisterFailureException.class)
    public ResponseEntity<ErrorResponse> handleException(RegisterFailureException e) {
        switch (e.getReason()) {
            case EMAIL_ALREADY_EXISTS:
                return ResponseEntity.badRequest().body(ErrorResponse.builder()
                        .message("이미 존재하는 이메일 입니다!")
                        .status(ErrorStatus.REGISTER_EMAIL_ALREADY_EXISTS)
                        .build());
            case NAME_ALREADY_EXISTS:
                return ResponseEntity.badRequest().body(ErrorResponse.builder()
                        .message("이미 존재하는 이름 입니다!")
                        .status(ErrorStatus.REGISTER_NAME_ALREADY_EXISTS)
                        .build());
            default:
                return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .message("회원가입중 오류가 발생하였습니다!")
                .status(ErrorStatus.REGISTER_ERROR)
                .build());
        }
    }
}
