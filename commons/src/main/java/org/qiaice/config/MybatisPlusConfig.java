package org.qiaice.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        // 初始化核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 初始化分页插件
        // 如果有多数据源可以不配置具体类型，否则都建议配上具体的 DbType
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();

        // 设置分页上限
        paginationInnerInterceptor.setMaxLimit(1000L);

        // 添加分页插件
        // 如果配置多个插件，切记分页插件最后添加
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
}
