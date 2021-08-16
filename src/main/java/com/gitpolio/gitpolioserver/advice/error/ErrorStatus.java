package com.gitpolio.gitpolioserver.advice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {
    REGISTER_ERROR(3000, "Register error"),
    REGISTER_ID_ALREADY_EXISTS(3001, "Register id already exists"),

    LOGIN_ERROR(4000, "Login error"),
    LOGIN_ID_NOT_FOUND(4001, "Login id not found"),
    LOGIN_WRONG_PASSWORD(4002, "Login password is wrong");

    private final Integer statusCode;
    private final String statusName;
}
