package com.dempProject.utill;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

        @Autowired
        private JavaMailSender javaMailSender;

        public void sendEmail(String toEmail, String subject, String body) {
            MimeMessagePreparator preparator = mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
                message.setFrom("farheenorbit@gmail.com");
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(body, true);
            };

            javaMailSender.send(preparator);
            System.out.println("Mail sent");
        }
    }

