package com.gitpolio.gitpolioserver.entity;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.modelmapper.ModelMapperUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

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
    private String name;

    @Length(max = 50)
    private String email;

    @Length(max = 128)
    private String password;

    @Length(max = 255)
    @Column(name = "auth_token")
    private String githubToken;

    public AccountDto toDto() {
        ModelMapper modelMapper = ModelMapperUtils.getModelMapper();

        AccountDto account = modelMapper.map(this, AccountDto.class);
        account.setEncodedPassword(password);

        return account;
    }
}
