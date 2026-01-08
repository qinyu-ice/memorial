package org.qinyu.config;

import org.qinyu.service.client.MartyrClient;
import org.qinyu.service.client.UserClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RecordWebMvcConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public UserClient userClient(RestClient.Builder builder) {
        RestClient client = builder.defaultHeader("X-Server-Key", "user").baseUrl("http://user-service").build();
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserClient.class);
    }

    @Bean
    public MartyrClient martyrClient(RestClient.Builder builder) {
        RestClient client = builder.defaultHeader("X-Server-Key", "martyr").baseUrl("http://martyr-service").build();
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(MartyrClient.class);
    }
}
