package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
import com.lettrip.lettripbackend.util.RedisUtil;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.data.redis.expireMinutes}")
    private long expireMin;
    @Value("${spring.mail.from}")
    private String fromAddress;

    public void sendMail(String email, MimeMessage message) throws Exception{
        try{
            javaMailSender.send(message);
        } catch(MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    @Transactional
    public void sendVerificationEmail(String email) throws Exception {
        String code = createRandomCode(6);
        MimeMessage mimeMessage = createVerificationMessage(email,code);
        sendMail(email,mimeMessage);
        redisUtil.setDataExpire(code,email,expireMin);
    }

    private MimeMessage createVerificationMessage(String email, String code) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO,email);
        message.setSubject("Lettrip 이메일 인증 코드입니다.");
        message.setText(createVerificationEmailText(code),"utf-8","html");
        message.setFrom(new InternetAddress(fromAddress,"Lettrip"));
        return message;
    }

    private String createVerificationEmailText(String code) {
        String text = "";
        text += "아래 코드를 Lettrip 이메일 인증 코드란에 입력해주세요.";
        text += "<br>";
        text += "CODE : <strong>";
        text += code;
        text += "</strong>";

        return text;
    }

    @Transactional
    public void verifyEmailCode(String code) {
        String email = redisUtil.getData(code);
        if(email==null) {
            throw new LettripException(LettripErrorCode.EMAIL_CODE_NOT_MATCH);
        }
        redisUtil.deleteData(code);
    }

    public static String createRandomCode(int length) {
        return UUID.randomUUID().toString().substring(0,length);
    }
}

