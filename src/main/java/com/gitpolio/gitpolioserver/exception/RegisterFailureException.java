package com.gitpolio.gitpolioserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterFailureException extends RuntimeException {
    private final Reason reason;

    public enum Reason {
        EMAIL_ALREADY_EXISTS
    }
}
