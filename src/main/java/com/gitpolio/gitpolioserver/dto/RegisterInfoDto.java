package com.gitpolio.gitpolioserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
public class RegisterInfoDto {
    @NotBlank
    private final String name, email;
    //raw password
    @Size(max = 25)
    private final String password;
    @NotBlank
    private final String githubToken;
}
