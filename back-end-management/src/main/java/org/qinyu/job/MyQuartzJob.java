package org.qinyu.job;

import jakarta.annotation.Resource;
import org.qinyu.mapper.UserMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义定时任务类
 */
@Component
public class MyQuartzJob implements Job {

    // 核心：使用 AtomicInteger 保证线程安全的累加（静态变量保留状态）
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Resource
    UserMapper userMapper;

    /**
     * 任务执行的核心方法
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 获取任务参数（可选）
        String jobParam = context.getJobDetail().getJobDataMap().getString("param");

        // 核心操作：计数 +1，并获取最新值
        int currentValue = COUNTER.incrementAndGet();

        int userCount = userMapper.selectCount();

        // 具体的任务逻辑（示例：打印当前时间和参数）
        System.out.println("定时任务执行中 >> 时间：" + LocalDateTime.now()
                + " | 当前计数值：" + currentValue
                + " | 当前用户数量：" + userCount
                + " | 当前剩余可创建用户数量：" + (Integer.MAX_VALUE - userCount));
    }
}