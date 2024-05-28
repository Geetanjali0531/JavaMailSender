package com.mailsender;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")

public class MailController {

    EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestParam String to,
                                           @RequestParam String subject,
                                           @RequestParam String body) {

        try {
            emailService.sendSimpleEmail(to, subject, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Mail Successfully Send");
    }



    @PostMapping("/sendEmailWithImage")
    public String sendEmailWithImage(@RequestParam String to,
                                     @RequestParam String subject,
                                     @RequestParam String text,
                                     @RequestParam MultipartFile imageFile) {
        emailService.sendEmailWithImage(to, subject, text, imageFile);
        return "Email sent successfully!";
    }

}
