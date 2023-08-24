package com.example.RH.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender emailSender;

    public void sentSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("marnissiarafet@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list!=null && list.size()>0){
            message.setCc(getCcArray(list));
            emailSender.send(message);
        }
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for (int i = 0;i< ccList.size();i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }
    public void forgotMail(String to,String subject ,String token) throws MessagingException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom("marnissiarafet@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your rest password link for Rh System</b><br><b>Email: </b> " + to +  "<br><a href=\"http://localhost:4200/createnewPassword?token="+token+"\">"+"Click here to login</a></p><br><h4>this link is valid only for 5 min</h4>";
        message.setContent(htmlMsg,"text/html");
        emailSender.send(message);
    }
}
