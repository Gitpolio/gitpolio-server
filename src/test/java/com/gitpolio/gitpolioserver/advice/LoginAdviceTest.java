package com.gitpolio.gitpolioserver.advice;

import com.gitpolio.gitpolioserver.advice.error.ErrorResponse;
import com.gitpolio.gitpolioserver.advice.error.ErrorStatus;
import com.gitpolio.gitpolioserver.exception.LoginFailureException;
import com.gitpolio.gitpolioserver.exception.UnknownIdTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginAdviceTest {
    private LoginAdvice loginAdvice;
    @BeforeEach
    public void init() {
        loginAdvice = new LoginAdvice();
    }

    @Test
    public void testUnknownIdException() {
        UnknownIdTypeException exception = mock(UnknownIdTypeException.class);
        ResponseEntity<ErrorResponse> response =  loginAdvice.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), ErrorStatus.LOGIN_ERROR);
    }

    @Test
    public void testLoginFailureException1() {
        LoginFailureException exception = mock(LoginFailureException.class);
        when(exception.getReason()).thenReturn(LoginFailureException.Reason.ID_NOT_FOUND);
        ResponseEntity<ErrorResponse> response = loginAdvice.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), ErrorStatus.LOGIN_ID_NOT_FOUND);
    }

    @Test
    public void testLoginFailureException2() {
        LoginFailureException exception = mock(LoginFailureException.class);
        when(exception.getReason()).thenReturn(LoginFailureException.Reason.WRONG_PASSWORD);
        ResponseEntity<ErrorResponse> response = loginAdvice.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), ErrorStatus.LOGIN_WRONG_PASSWORD);
    }
}
