package com.example.smartjobportalsystem.service;

import com.example.smartjobportalsystem.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailDTO email){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(email.getToEmail());
        simpleMailMessage.setSubject(email.getSubject());
        simpleMailMessage.setText(email.getDescription());

        //send with above details
        mailSender.send(simpleMailMessage);
    }

    public void verificationEmail(EmailDTO email){
        SimpleMailMessage smm=new SimpleMailMessage();
        smm.setTo(email.getToEmail());
        smm.setSubject(email.getSubject());
        smm.setText(email.getDescription());
    }
}
