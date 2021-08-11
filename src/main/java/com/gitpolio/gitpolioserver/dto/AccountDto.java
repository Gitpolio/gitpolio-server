package com.gitpolio.gitpolioserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter @Builder
public class AccountDto {
    private final String name, email, encodedPassword;
    private final String githubToken;
}
