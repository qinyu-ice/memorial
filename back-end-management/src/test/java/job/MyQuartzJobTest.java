package job;

import org.qinyu.job.MyQuartzJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Quartz 定时任务调度器
 */
public class MyQuartzJobTest {

    public static void main(String[] args) throws SchedulerException {
        // 1. 创建 Scheduler（调度器）
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 2. 定义 JobDetail（绑定具体的 Job 类）
        JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class)
                .withIdentity("myJob", "group1") // 任务唯一标识（名称 + 分组）
                .usingJobData("param", "测试参数") // 设置任务参数（可选）
                .build();

        // 3. 定义 Trigger（触发规则）
        // 示例1：简单触发器 - 每隔3秒执行一次，无限循环
        Trigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity("simpleTrigger", "group1") // 触发器唯一标识
                .startNow() // 立即启动
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(3) // 执行间隔
//                        .withRepeatCount(5)) // 重复执行5次，加上首次执行，总共执行6次
                        .repeatForever()) // 无限重复
                .build();

        // 示例2：Cron 触发器 - 更灵活的时间规则（每分钟的第0秒执行）
        // Cron 表达式（常用示例）：
        // 0/3 * * * * ?：每 3 秒执行一次
        // 0 0 10 * * ?：每天上午 10 点执行
        // 0 0 10,14,16 * * ?：每天 10 点、14 点、16 点执行
        // 0 0/30 9-17 * * ?：朝九晚五期间，每 30 分钟执行一次
        // Trigger cronTrigger = TriggerBuilder.newTrigger()
        //         .withIdentity("cronTrigger", "group1")
        //         .startNow()
        //         .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // Cron 表达式
        //         .build();

        // 4. 将 Job 和 Trigger 绑定到 Scheduler
        scheduler.scheduleJob(jobDetail, simpleTrigger);

        // 5. 启动调度器
        scheduler.start();

        // 可选：防止主线程退出（实际项目中无需此代码，因为项目本身是常驻进程）
        try {
            Thread.sleep(60000); // 让程序运行1分钟后再停止
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 6. 关闭调度器（实际项目中一般不主动关闭）
        // scheduler.shutdown();
    }
}