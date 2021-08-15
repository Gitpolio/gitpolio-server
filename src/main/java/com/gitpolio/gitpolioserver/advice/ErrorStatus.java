package com.gitpolio.gitpolioserver.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum ErrorStatus {
    A(1);
    private final int id;
}
