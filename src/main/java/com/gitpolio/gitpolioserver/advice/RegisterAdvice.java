package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.advice.error.ErrorResponse;
import com.gitpolio.gitpolioserver.advice.error.ErrorStatus;
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
    public ResponseEntity<ErrorResponse> handlingException(RegisterFailureException e) {
        switch (e.getReason()) {
            case EMAIL_ALREADY_EXISTS:
                return ResponseEntity.badRequest().body(ErrorResponse.builder()
                        .message("이미 존재하는 아이디 입니다!")
                        .status(ErrorStatus.REGISTER_ID_ALREADY_EXISTS)
                        .build());
            default:
                return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .message("회원가입중 오류가 발생하였습니다!")
                .status(ErrorStatus.REGISTER_ERROR)
                .build());
        }
    }
}
