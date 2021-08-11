package com.gitpolio.gitpolioserver.entity;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Account {
    @Builder
    public Account(@Length(max = 18) String name,
                   @Length(max = 50) String email,
                   @Length(max = 25) String password,
                   @Length(max = 255) String githubToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.githubToken = githubToken;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 18)
    @Setter private String name;

    @Length(max = 50)
    @Setter private String email;

    @Length(max = 25)
    @Setter private String password;

    @Length(max = 255)
    @Column(name = "auth_token")
    @Setter private String githubToken;

    public AccountDto toDto() {
        return AccountDto.builder()
                .name(name)
                .email(email)
                .encodedPassword(password)
                .githubToken(githubToken)
                .build();
    }
}
