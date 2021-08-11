package com.gitpolio.gitpolioserver.service;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.entity.Account;
import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import com.gitpolio.gitpolioserver.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    public AccountDto register(RegisterInfoDto registerInfo) {
        //이미 해당 이메일로 가입한 계정이 있을경우 예외를 throw 한다
        String email = registerInfo.getEmail();
        if(accountRepository.existsByEmail(email)) throw new RegisterFailureException();
        //password encoder 로 사용자가 보낸 raw password 를 encoding 한다
        String encodedPassword = passwordEncoder.encode(registerInfo.getPassword());
        //가공된 정보들을 Database 에 저장 하고 저장된 값들을 반환한다
        Account account = Account.builder()
                .name(registerInfo.getName())
                .email(registerInfo.getEmail())
                .password(encodedPassword)
                .githubToken(registerInfo.getGithubToken())
                .build();
        accountRepository.save(account);
        return account.toDto();
    }
}
