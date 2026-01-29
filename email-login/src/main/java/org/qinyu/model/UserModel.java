package org.qinyu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Slf4j
@Data
@Entity(name = "user")
@JsonIgnoreProperties(value = {
        "authorities", "accountNonExpired", "accountNonLocked",
        "credentialsNonExpired", "enabled", "log", "emailPassword", "email"
})
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(nullable = false, length = 64, unique = true)
    private String email;

    @Column(nullable = false, length = 64)
    private String emailPassword;

    @Transient
    private String token;

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public @NonNull String getPassword() {
        return emailPassword;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }

    public static UserModel withEmail(String email) {
        UserModel model = new UserModel();
        model.setEmail(email);
        return model;
    }

    public Map<String, Object> payload() {
        Class<? extends UserModel> clazz = this.getClass();
        List<String> ignoreProperties = Arrays.asList(clazz.getAnnotation(JsonIgnoreProperties.class).value());
        Map<String, Object> payload = new HashMap<>();
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    Transient aTransient = field.getAnnotation(Transient.class);
                    if (Objects.nonNull(aTransient)) {
                        return;
                    }
                    String name = field.getName();
                    if (ignoreProperties.contains(name)) {
                        return;
                    }
                    try {
                        field.setAccessible(true);
                        payload.put(name, field.get(this));
                    } catch (IllegalAccessException exception) {
                        log.warn("无法生成 jwt 负载的 {} 字段", name);
                    }
                });
        return payload;
    }

    public Map<String, Object> record() {
        Map<String, Object> record = new HashMap<>();
        record.put("id", id);
        record.put("username", username);
        return record;
    }
}
