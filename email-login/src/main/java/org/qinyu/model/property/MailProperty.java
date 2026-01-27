package org.qinyu.model.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "spring.thymeleaf.properties")
public record MailProperty(String template, String from, String subject, String defaults) {
}
