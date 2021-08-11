package com.gitpolio.gitpolioserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RegisterInfoDto {
    private final String name, email, password;
    private final String githubToken;
}
