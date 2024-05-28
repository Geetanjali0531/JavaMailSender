package com.mailsender;

import com.mailsender.exception.EmailSendingException;
import com.mailsender.exception.FileProcessingException;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);


    }


    public void sendEmailWithImage(String to, String subject, String text, MultipartFile imageFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            // Attach the image
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.setDataHandler(new DataHandler(new ByteArrayDataSource(imageFile.getBytes(), "image/jpeg")));
            imagePart.setFileName("image.jpg");

            // Add the image attachment to the email
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(imagePart);
            message.setContent(multipart);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendingException("Error sending email", e);
        } catch (IOException e) {
            throw new FileProcessingException("Error processing image file", e);
        }
    }

}
