package com.gitpolio.gitpolioserver.service;

import com.gitpolio.gitpolioserver.domain.IdType;
import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.LoginInfoDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.entity.Account;
import com.gitpolio.gitpolioserver.exception.LoginFailureException;
import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import com.gitpolio.gitpolioserver.exception.UnknownIdTypeException;
import com.gitpolio.gitpolioserver.jwt.AuthTokenUtils;
import com.gitpolio.gitpolioserver.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountDto register(RegisterInfoDto registerInfo) {
        //이미 해당 이메일로 가입한 계정이 있을경우 예외를 throw 한다
        String email = registerInfo.getEmail();
        if(accountRepository.existsByEmail(email)) throw new RegisterFailureException(
                RegisterFailureException.Reason.EMAIL_ALREADY_EXISTS);
        //password encoder 로 사용자가 보낸 raw password 를 encoding 한다
        String encodedPassword = passwordEncoder.encode(registerInfo.getPassword());
        //가공된 정보들을 Database 에 저장 하고 저장된 값들을 반환한다
        Account account = Account.builder()
                .name(registerInfo.getName())
                .email(registerInfo.getEmail())
                .encodedPassword(encodedPassword)
                .githubToken(registerInfo.getGithubToken())
                .build();
        try {
            accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new RegisterFailureException(RegisterFailureException.Reason.NAME_ALREADY_EXISTS);
        }
        return account.toDto();
    }

    public String login(LoginInfoDto loginInfo) {
        Account account = validateIdAndGetAccount(loginInfo.getId());//Id 검증 & db 조회를 통한 account 초기화
        validatePassword(account, loginInfo.getRawPassword());//Password 검증
        return AuthTokenUtils.generateJwtToken(loginInfo);//Token 생성 및 반환
    }

    //Id 검증 & account 조회 로직
    private Account validateIdAndGetAccount(String id) {
        IdType idType;
        if (accountRepository.existsByName(id)) idType = IdType.NAME;
        else if (accountRepository.existsByEmail(id)) idType = IdType.EMAIL;
        else throw new LoginFailureException(
                LoginFailureException.Reason.ID_NOT_FOUND);

        switch (idType) {
            case EMAIL:
                return accountRepository.getByEmail(id);
            case NAME:
                return accountRepository.getByName(id);
            default:
                throw new UnknownIdTypeException(idType);
        }
    }

    //Password 검증 로직
    private void validatePassword(Account account, String rawPassword) {
        boolean isPasswordRight = passwordEncoder.matches(rawPassword, account.getEncodedPassword());

        if (!isPasswordRight) throw new LoginFailureException(
                LoginFailureException.Reason.WRONG_PASSWORD);
    }
}
