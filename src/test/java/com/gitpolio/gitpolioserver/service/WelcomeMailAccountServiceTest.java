package com.gitpolio.gitpolioserver.service;

import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.exception.WelcomeMailException;
import com.gitpolio.gitpolioserver.repository.AccountRepository;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class WelcomeMailAccountServiceTest {
    private WelcomeMailAccountService welcomeMailAccountService;
    private MailSenderService mailSenderService;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    private LoremIpsum lorem;

    @BeforeEach
    public void init() {
        mailSenderService = mock(MailSenderService.class);
        accountRepository = mock(AccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        welcomeMailAccountService = new WelcomeMailAccountService(accountRepository, passwordEncoder, mailSenderService);

        lorem = LoremIpsum.getInstance();
    }

    @Test
    public void testIsRegisterSendWelcomeMail() throws MessagingException {
        //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfo = getRandomRegisterInfo();

        //이메일 중복여부 확인과 계정 저장이 제대로 되었는지 확인하기 위해서 account repository 를 mocking 한다
        when(accountRepository.existsByEmail(registerInfo.getEmail())).thenReturn(false);
        //~002 테스트 대상 실행
        welcomeMailAccountService.register(registerInfo);
        //~003 테스트 대상 검사
        verify(mailSenderService, times(1))
                .sendEmail(eq(registerInfo.getEmail()), anyString(), anyString());
    }

    @Test
    public void testIsRegisterThrowWelcomeMailException() throws MessagingException {
        //~001 테스트 환경 구성
        //Random data 가 담긴 RegisterInfoDto 를 가져온다
        RegisterInfoDto registerInfo = getRandomRegisterInfo();
        //이메일 발송중 Messaging Exception 이 throw 되게 환경 구성
        MessagingException exception = mock(MessagingException.class);
        doThrow(exception).when(mailSenderService).sendEmail(anyString(), anyString(), anyString());

        //~002 ~003 테스트 대상 실행 & 테스트 대상 검사
        //throw 된 Exception 의 cause 에 기존에 throw 된 MessagingException 이 제대로 들어가있는가 (3)
        assertEquals(
                //이메일 발송중 Messaging Exception 이 throw 될 경우 (1)
                // RuntimeException 인 WelcomeMailException 으로 Wrapping 하여 throw 하였는가 (2)
                assertThrows(WelcomeMailException.class, () ->
                        welcomeMailAccountService.register(registerInfo)).getCause(), exception);
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
