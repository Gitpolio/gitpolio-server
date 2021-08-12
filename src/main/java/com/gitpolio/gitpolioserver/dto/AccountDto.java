package com.gitpolio.gitpolioserver.dto;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class AccountDto {
    private String name, email, encodedPassword;
    private String githubToken;
}
