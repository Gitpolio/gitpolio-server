package com.gitpolio.gitpolioserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class LoginInfoDto {
    @NotBlank
    private final String id;
    //raw password
    @NotBlank @JsonProperty("password")
    private final String rawPassword;
}
