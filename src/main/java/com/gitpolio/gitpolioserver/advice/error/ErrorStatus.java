package com.gitpolio.gitpolioserver.advice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter @ToString
@RequiredArgsConstructor
public enum ErrorStatus {
    REGISTER_ERROR(3000, "Register error"),
    REGISTER_WRONG_REGISTER_INFO(3001, "Wrong register info"),
    REGISTER_EMAIL_ALREADY_EXISTS(3002, "Register id already exists"),

    LOGIN_ERROR(4000, "Login error"),
    LOGIN_ID_NOT_FOUND(4001, "Login id not found"),
    LOGIN_WRONG_PASSWORD(4002, "Login password is wrong");

    private final Integer statusCode;
    private final String statusName;
}
