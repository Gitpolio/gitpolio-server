package com.gitpolio.gitpolioserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class RegisterInfoDto {
    @NotBlank
    private final String name, email;
    //raw password
    @Max(25)
    private final String password;
    @NotBlank
    private final String githubToken;
}
