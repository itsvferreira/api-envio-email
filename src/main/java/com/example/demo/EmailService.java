package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${sendgrid.sender.email}")
    private String senderEmail;

    @Value("${sendgrid.receiver.email}")
    private String receiversEmail;

    private final String templatePath = "templates/";

    public void sendEmails(EmailDTO emailDto) {
        sendEmail(buildApplicantEmail(emailDto));
        sendEmail(buildSectorEmail(emailDto));
    }

    private Mail buildSectorEmail(EmailDTO emailDto) {
        String html = readAndReplaceLabels(
                templatePath + "setor_modelo_2.html",
                Map.of(
                        "{{nome}}", emailDto.name(),
                        "{{email}}", emailDto.email(),
                        "{{telefone}}", emailDto.phone(),
                        "{{data}}", emailDto.date(),
                        "{{hora}}", emailDto.time()));

        return buildMail(senderEmail, List.of(receiversEmail.split(", ")),
                "Nova solicitação de visita", html);
    }

    private Mail buildApplicantEmail(EmailDTO emailDto) {
        String html = readAndReplaceLabels(
                templatePath + "solicitante_modelo_3.html",
                Map.of("{{nome}}", emailDto.name()));

        return buildMail(senderEmail, List.of(emailDto.email()), "Solicitação de visita registrada", html);
    }

    private Mail buildMail(String from, List<String> tos, String subject, String html) {
        Mail mail = new Mail();
        mail.setFrom(new Email(from));
        mail.setSubject(subject);
        mail.addContent(new Content("text/html", html));

        Personalization personalization = new Personalization();
        tos.forEach(to -> personalization.addTo(new Email(to)));
        mail.addPersonalization(personalization);

        return mail;
    }

    private void sendEmail(Mail mail) {
        try {
            SendGrid sendGrid = new SendGrid(apiKey);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException e) {
            throw new IllegalStateException("Falha no envio de e-mail", e);
        }
    }

    private String readAndReplaceLabels(String path, Map<String, String> values) {
        String template = readTemplate(path);
        for (Map.Entry<String, String> label : values.entrySet()) {
            template = template.replace(label.getKey(), label.getValue());
        }
        return template;
    }

    private String readTemplate(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new IOException().getMessage();
        }
    }
}
