package com.gitpolio.gitpolioserver.service;

import com.gitpolio.gitpolioserver.dto.AccountDto;
import com.gitpolio.gitpolioserver.dto.RegisterInfoDto;
import com.gitpolio.gitpolioserver.exception.WelcomeMailException;
import com.gitpolio.gitpolioserver.repository.AccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service @Primary
public class WelcomeMailAccountService extends AccountService{
    private final MailSenderService mailSenderService;
    public WelcomeMailAccountService(AccountRepository accountRepository,
                                     PasswordEncoder passwordEncoder,
                                     MailSenderService mailSenderService) {
        super(accountRepository, passwordEncoder);
        this.mailSenderService = mailSenderService;
    }

    @Override
    public AccountDto register(RegisterInfoDto registerInfo) {
        AccountDto dto = super.register(registerInfo);
        sendWelcomeMail(registerInfo);
        return dto;
    }

    private void sendWelcomeMail(RegisterInfoDto registerInfo) {
        String to = registerInfo.getEmail();
        String subject = String.format("[Gitpolio] %s 님의 가입을 환영합니다!", registerInfo.getName());

        String name = registerInfo.getName();
        String text = "<h2>" + name + "님 Gitpolio 에 가입하신것을 환영합니다!</h2>안녕하세요" + name +
                "님! Gitpolio 에 가입하신것을 환영합니다 \uD83C\uDF89\uD83C\uDF89<p>그냥그렇다고요. " +
                "그럼 전 또 가입환영메일을 보내러 이만...";
        try {
            mailSenderService.sendEmail(to, subject, text);
        } catch (MessagingException e) {
            throw new WelcomeMailException(e);
        }
    }
}
