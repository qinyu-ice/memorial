package org.qinyu.config;

import org.qinyu.model.property.JwtProperty;
import org.qinyu.model.property.MailProperty;
import org.qinyu.model.property.RedisProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { JwtProperty.class, MailProperty.class, RedisProperty.class })
public class PropertyConfig {
}
