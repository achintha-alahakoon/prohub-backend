package com.inn.incident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendApplicationReceivedEmail(String toEmail, String applicantName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Application Received - Thank You!");
        message.setText("Dear " + applicantName + ",\n\n"
                + "Thank you for applying to our job opening. We have received your application. "
                + "Our team will review it and get back to you if you are selected for the next stage.\n\n"
                + "Best Regards,\n"
                + "HR Team");

        mailSender.send(message);
    }
}

