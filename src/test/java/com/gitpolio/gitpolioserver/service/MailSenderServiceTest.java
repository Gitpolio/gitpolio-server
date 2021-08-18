package com.gitpolio.gitpolioserver.service;

import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MailSenderServiceTest {
    private JavaMailSender javaMailSender;
    private MailSenderService mailSenderService;

    private LoremIpsum lorem;

    @BeforeEach
    public void init() {
        javaMailSender = mock(JavaMailSender.class);
        mailSenderService = new MailSenderService(javaMailSender);

        lorem = LoremIpsum.getInstance();
    }

    @Test
    public void testSendEmailBySimpleMailMessage() {
        //~001 테스트 환경 구성
        //mail sender service 로 보낼 메세지를 설정한다
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(lorem.getEmail());
        message.setSubject(lorem.getTitle(1));
        message.setText(lorem.getWords(10, 20));
        message.setSentDate(new Date(new Random().nextLong()));

        //~002 테스트 대상 실행
        mailSenderService.sendEmail(message);

        //~003 테스트 대상 검사
        verify(javaMailSender, times(1)).send(message);
    }

    @Test
    public void testSendEmailByInformation() throws MessagingException {
        //~001 테스트 환경 구성
        //mail sender service 로 보낼 메세지를 설정한다
        String to = lorem.getEmail();
        String subject = lorem.getTitle(1);
        String message = lorem.getWords(10, 20);

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        //~003 테스트 대상 검사
        //doAnswer 를 통해 대상 실행시 자동으로 이후에 테스트 대상을 검사하도록 설정한다
        doAnswer(invocation-> {
            MimeMessage msg = invocation.getArgument(0);
            assertEquals(mimeMessage, msg);
            return null;
        }).when(javaMailSender).send(any(MimeMessage.class));

        //~002 테스트 대상 실행
        mailSenderService.sendEmail(to, subject, message);
    }
}
