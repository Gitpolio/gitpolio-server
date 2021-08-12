package com.gitpolio.gitpolioserver.controller;

import com.gitpolio.gitpolioserver.dto.AccountDto;
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
        RegisterInfoDto registerInfoDto = getRandomRegisterInfo();

        AccountDto account = mock(AccountDto.class);
        when(accountService.register(registerInfoDto)).thenReturn(account);

        ResponseEntity<AccountDto> responseEntity = accountController.register(registerInfoDto);

        verify(accountService, times(1)).register(registerInfoDto);

        assertEquals(account, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
