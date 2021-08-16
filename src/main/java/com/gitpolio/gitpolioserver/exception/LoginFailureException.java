package com.gitpolio.gitpolioserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginFailureException extends RuntimeException {
    private final Reason reason;

    public enum Reason {
        ID_NOT_FOUND, WRONG_PASSWORD
    }
}
