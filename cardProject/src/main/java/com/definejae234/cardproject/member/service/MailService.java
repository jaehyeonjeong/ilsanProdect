package com.definejae234.cardproject.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MemberService memberService;

    public void sendMail(){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setFrom("jjyy0614@naver.com");
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO,"jjy0614@nate.com");
            mimeMessage.setSubject("비밀번호 변경");
            mimeMessage.setText("Mail Test","utf-8","html");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendHelperMail(){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setFrom("jjyy0614@naver.com","이름확인");
            helper.setTo("jjy0614@nate.com");
            helper.setSubject("비밀번호변경");
            helper.setText("<h1><a href='http://localhost:8080/member/login'>링크</a></h1>",true);
            File file = new File("\"C:\\Users\\acer\\Pictures\\Screenshots\\스크린샷 2025-10-22 143723.png\"");
            helper.addAttachment("캐릭터",file);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChangePasswordMain(String userEmail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setFrom("jjyy0614@naver.com","이름확인");
            helper.setTo(userEmail);
            helper.setSubject("비밀번호변경");
            String randomNum = makeRandom();
            helper.setText("<h1 style='text-align:center; color:red'>"+randomNum+"</h1>",true);
            mailSender.send(mimeMessage);
            memberService.changePassword(randomNum,userEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    private String makeRandom(){
        Random random = new Random();
        int num = 10000 + random.nextInt(900000);
        return Integer.toString(num);
    }
    public void sendFindedIDMain(String userEmail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setFrom("jjyy0614@naver.com","이름확인");
            helper.setTo(userEmail);
            helper.setSubject("아이디찾기");
            String findedID = memberService.findedByUserID(userEmail);
            helper.setText("<h1 style='text-align:center; color:red'>"+findedID+"</h1>",true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}