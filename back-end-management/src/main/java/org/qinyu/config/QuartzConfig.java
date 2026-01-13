package org.qinyu.config;

import org.qinyu.job.MyQuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz 核心配置类
 * 定义 JobDetail（任务）和 Trigger（触发器）
 */
@Configuration
public class QuartzConfig {

    /**
     * 定义 JobDetail：绑定自定义的 Job 类
     */
    @Bean
    public JobDetail springBootQuartzJobDetail() {
        // 存储持久化时，jobDataAsMap 中的数据会被序列化
        return JobBuilder.newJob(MyQuartzJob.class)
                .withIdentity("springBootQuartzJob", "quartzGroup") // 任务唯一标识
                .usingJobData("bizParam", "Spring Boot 定时任务参数") // 设置任务参数
                .storeDurably() // 即使没有 Trigger 关联也保留该 JobDetail
                .build();
    }

    /**
     * 定义 Trigger：触发规则（两种常用方式二选一）
     */
    // 方式1：简单触发器（每隔3秒执行一次，无限循环）
//    @Bean
//    public Trigger simpleTrigger() {
//        return TriggerBuilder.newTrigger()
//                .forJob(springBootQuartzJobDetail()) // 绑定对应的 JobDetail
//                .withIdentity("simpleTrigger", "quartzGroup") // 触发器唯一标识
//                .startNow() // 立即启动
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInSeconds(3) // 执行间隔
//                        .repeatForever()) // 无限重复
//                .build();
//    }

    // 方式2：Cron 触发器（每分钟第0秒执行，更灵活）
    @Bean
    public Trigger cronTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(springBootQuartzJobDetail())
                .withIdentity("cronTrigger", "quartzGroup")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?")) // Cron 表达式（每3秒执行一次）
                .build();
    }
}