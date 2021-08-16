package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.advice.error.ErrorResponse;
import com.gitpolio.gitpolioserver.advice.error.ErrorStatus;
import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterAdviceTest {
    private RegisterAdvice registerAdvice;
    @BeforeEach
    public void init() {
        registerAdvice = new RegisterAdvice();
    }

    @Test
    public void testMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        ResponseEntity<ErrorResponse> response =  registerAdvice.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), ErrorStatus.REGISTER_WRONG_REGISTER_INFO);
    }

    @Test
    public void testRegisterFailureException() {
        RegisterFailureException exception = mock(RegisterFailureException.class);
        when(exception.getReason()).thenReturn(RegisterFailureException.Reason.EMAIL_ALREADY_EXISTS);
        ResponseEntity<ErrorResponse> response =  registerAdvice.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), ErrorStatus.REGISTER_EMAIL_ALREADY_EXISTS);
    }
}
