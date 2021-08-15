package com.gitpolio.gitpolioserver.dto;

import com.gitpolio.gitpolioserver.annotation.AccountName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@Getter
public class RegisterInfoDto {
    @NotBlank @AccountName
    private final String name;
    @NotBlank @Email
    private final String email;
    //raw password
    @Size(max = 25)
    private final String password;
    @NotBlank
    private final String githubToken;
}
