package org.qinyu.config;

import org.qinyu.service.client.MartyrClient;
import org.qinyu.service.client.UserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class RecordWebMvcConfig {

    // 配置请求工厂，设置超时时间
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));  // 连接超时
        factory.setReadTimeout(Duration.ofSeconds(5));     // 读取超时
        return factory;
    }

    // 移除 @LoadBalanced 注解，因为不需要负载均衡了
    @Bean
    public RestClient.Builder restClientBuilder(ClientHttpRequestFactory requestFactory) {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json");
    }

    // 直接指定 user-service 的 IP+端口
    @Bean
    public UserClient userClient(RestClient.Builder builder) {
        RestClient client = builder
                .defaultHeader("X-Server-Key", "user")
                // 替换为你的 user-service 实际地址和端口，比如 localhost:8080
                .baseUrl("http://localhost:8101")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserClient.class);
    }

    // 直接指定 martyr-service 的 IP+端口
    @Bean
    public MartyrClient martyrClient(RestClient.Builder builder) {
        RestClient client = builder
                .defaultHeader("X-Server-Key", "martyr")
                // 替换为你的 martyr-service 实际地址和端口
                .baseUrl("http://localhost:8201")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(MartyrClient.class);
    }
}