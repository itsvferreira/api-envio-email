package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDto) {
        try {
            emailService.sendEmails(emailDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("E-mail enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
