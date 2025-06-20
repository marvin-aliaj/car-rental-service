package com.spring.jdbc.car.rental.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailSenderService {
    InputStream inputStream = new ClassPathResource("templates/booking-confirmation.html").getInputStream();

    @Autowired
    private JavaMailSender mailSender;

    public EmailSenderService () throws IOException {
    }

    public void sendSimpleEmail (Map<String, String> variables, String toEmail, String subject) throws IOException, MessagingException {
        String template = new String (inputStream.readAllBytes (), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : variables.entrySet ()) {
            template = template.replace ("${" + entry.getKey () + "}", entry.getValue ());
        }

        // Send the email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper (message, true, "UTF-8");

        helper.setTo (toEmail);
        helper.setSubject (subject);
        helper.setText (template, true);

        mailSender.send (message);
    }
}
