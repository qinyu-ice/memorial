package org.qinyu.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.qinyu.model.property.MailProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender sender;
    private final TemplateEngine engine;
    private final MailProperty property;

    @Value(value = "${spring.data.redis.properties.timeout}")
    private Long timeout;

    @Async
    public void sendAuthCode(String email, String code) {
        Context context = new Context(Locale.SIMPLIFIED_CHINESE, context(email, code));
        String text = engine.process(property.template(), context);
        context.setVariable("text", text);
        sender.send(build(context));
    }

    private MimeMessage build(Context context) {
        MimeMailMessage mimeMessage = new MimeMailMessage(sender.createMimeMessage());
        mimeMessage.setFrom(property.from());
        mimeMessage.setSubject(property.subject());
        String email = context.getVariable("email").toString();
        mimeMessage.setTo(email);
        try {
            String text = context.getVariable("text").toString();
            mimeMessage.getMimeMessageHelper().setText(text, true);
        } catch (MessagingException exception) {
            String code = context.getVariable("code").toString();
            String text = String.format(property.defaults(), email, code, timeout);
            mimeMessage.setText(text);
        }
        return mimeMessage.getMimeMessage();
    }

    private Map<String, Object> context(String email, String code) {
        return Map.of("email", email, "code", code, "timeout", timeout);
    }
}
