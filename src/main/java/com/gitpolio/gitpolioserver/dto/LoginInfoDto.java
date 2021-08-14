package com.gitpolio.gitpolioserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class LoginInfoDto {
    @NotBlank
    private final String id;
    //raw password
    @NotBlank
    private final String rawPassword;
}
