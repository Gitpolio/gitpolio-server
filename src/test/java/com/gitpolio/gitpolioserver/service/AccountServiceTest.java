package com.gitpolio.gitpolioserver.service;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.entity.Account;
import com.gitpolio.gitpolioserver.exception.RegisterFailureException;
import com.gitpolio.gitpolioserver.repository.AccountRepository;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private LoremIpsum lorem;

    @BeforeEach
    public void init() {
        accountRepository = mock(AccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        accountService = new AccountService(accountRepository, passwordEncoder);
        lorem = LoremIpsum.getInstance();
    }

    /*
    회원가입시 다음과 같은 정보를 필요로 한다
    - 이름
    - 이메일
    - 암호
    - Github 토큰
    기존 유저와 이메일이 중복되지 않은경우, 암호를 Hashing 한뒤 DB 에 회원가입 정보로 얻은 유저 정보를 저장한다

    따라서, register method 는 RegisterInformation(name, email, password, token) 을 인자로 받고
    AccountDto(name, email, encoded password, token) 를 반환한다
     */@Test
    public void testRegister() {
         //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfo = getRandomRegisterInfo();

        //이메일 중복여부 확인과 계정 저장이 제대로 되었는지 확인하기 위해서 account repository 를 mocking 한다
        when(accountRepository.existsByEmail(registerInfo.getEmail())).thenReturn(false);
        //~003 테스트 대상 검사 (when - Answer 로 검사하기떄문에 테스트 대상 실행 이전에 setting 한다)
        when(accountRepository.save(any())).thenAnswer(invocation -> {
                    Account account = invocation.getArgument(0);

                    assertEquals(registerInfo.getName(), account.getName());
                    assertEquals(registerInfo.getEmail(), account.getEmail());
                    //encode 된 password 를 저장해야한다
                    assertEquals(passwordEncoder.encode(registerInfo.getPassword()), account.getPassword());
                    assertEquals(registerInfo.getGithubToken(), account.getGithubToken());

                    return null;
                }
        );

        //비밀번호가 제대로 encoding 되었는지 확인하기 위해서 password encoder 의 encode method 를 mocking 한다
        String salt = lorem.getWords(1);
        when(passwordEncoder.encode(registerInfo.getPassword())).thenReturn(registerInfo.getPassword() + salt);

        //~002 테스트 대상 실행
        //AccountService 의 register method 를 호출한다
        AccountDto account = accountService.register(registerInfo);

        //~003 테스트 대상 검사
        //db 에 저장한 값을 return 하였는지 검사한다
        assertEquals(registerInfo.getName(), account.getName());
        assertEquals(registerInfo.getEmail(), account.getEmail());
        assertEquals(passwordEncoder.encode(registerInfo.getPassword()), account.getEncodedPassword());
        assertEquals(registerInfo.getGithubToken(), account.getGithubToken());
    }

    /*
    이미 같은 이메일로 가입된 이메일이 있을경우, LoginFailureException 을 반환해야한다.
     */@Test
    public void testRegisterFailure() {
        //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfo = getRandomRegisterInfo();

        //이메일이 중복되어야 하므로 true 를 반환한다
        when(accountRepository.existsByEmail(registerInfo.getEmail())).thenReturn(true);

        //~002 테스트 대상 실행
        //테스트 중 RegisterFailureException 이 throwing 되는지 검사한다
        assertThrows(RegisterFailureException.class,
                () -> accountService.register(registerInfo));
    }

    public RegisterInfoDto getRandomRegisterInfo() {
        String name = lorem.getName();
        String email = lorem.getEmail();
        String password = lorem.getWords(1);
        String githubToken = new String(Base64Coder.encode(lorem.getWords(4).getBytes(StandardCharsets.UTF_8)));

        return new RegisterInfoDto(name, email, password, githubToken);
    }
}