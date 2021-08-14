package com.gitpolio.gitpolioserver.controller;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.LoginInfoDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.service.AccountService;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountControllerTest {
    private AccountController accountController;
    private AccountService accountService;
    private LoremIpsum lorem;

    @BeforeEach
    public void init() {
        accountService = mock(AccountService.class);
        accountController = new AccountController(accountService);
        lorem = LoremIpsum.getInstance();
    }

    @Test
    public void testRegister() {
        //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfoDto = getRandomRegisterInfo();

        //accountService 에 registerInfoDto 를 인자로 하는 register method 에 책임을 위임하였는지 검사하기위해
        // 해당 method 의 return value를 조작한다
        AccountDto account = mock(AccountDto.class);
        when(accountService.register(registerInfoDto)).thenReturn(account);

        //~002 테스트 대상 실행
        //반환되는 ResponseEntity 검사를 위해 return value 를 변수에 저장한다
        ResponseEntity<AccountDto> responseEntity = accountController.register(registerInfoDto);

        //accountService 의 register 가 호출되었는지 (책임이 위임되었는지) 검사한다
        verify(accountService, times(1)).register(registerInfoDto);

        //~003 테스트 대상 검사
        //책임을 위임할때 registerInfoDto 를 인자로 넘겨 주었는지,
        // service 의 return value 를 통해 client 에 응답한 ResponseEntity 를 잘 구성하였는지 검사한다
        assertEquals(account, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testLogin() {
        //~001 테스트 환경 구성
        LoginInfoDto loginInfoDto = getLoginInfoDto();
        String token = new String(Base64Coder.encode(lorem.getWords(3).getBytes(StandardCharsets.UTF_8)));

        when(accountService.login(loginInfoDto)).thenReturn(token);
        //~002 테스트 대상 실행
        ResponseEntity<String> responseEntity = accountController.login(loginInfoDto);

        verify(accountService, (times(1))).login(loginInfoDto);

        //~003 테스트 대상 검사
        assertEquals(token, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    public LoginInfoDto getLoginInfoDto() {
        String id = lorem.getEmail();
        String rawPassword = lorem.getWords(1);

        return new LoginInfoDto(id, rawPassword);
    }

    //Random data 로 구성된 RegisterInfoDto 를 생성하여 반환한다
    public RegisterInfoDto getRandomRegisterInfo() {
        String name = lorem.getName();
        String email = lorem.getEmail();
        String password = lorem.getWords(1);
        String githubToken = new String(Base64Coder.encode(lorem.getWords(4).getBytes(StandardCharsets.UTF_8)));

        return new RegisterInfoDto(name, email, password, githubToken);
    }
}
