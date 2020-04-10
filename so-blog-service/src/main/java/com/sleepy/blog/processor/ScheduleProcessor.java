package com.sleepy.blog.processor;

import com.sleepy.common.exception.UserOperationIllegalException;
import com.sleepy.common.tools.CommonTools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务处理器
 *
 * @author gehoubao
 * @create 2019-09-30 14:11
 **/
@Slf4j
@Component
public class ScheduleProcessor {

    private Scheduler scheduler;

    public ScheduleProcessor() {
        try {
            DirectSchedulerFactory schedulerFactory = DirectSchedulerFactory.getInstance();
            // 表示以3个工作线程初始化工厂
            schedulerFactory.createVolatileScheduler(3);
            scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    public void addJob(String jobName, String jobGroupName,
                       String triggerName, String triggerGroupName, Class jobClass, String cron, Object data) throws UserOperationIllegalException {
        try {
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            jobDetail.getJobDataMap().put("params", data);

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);

            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            log.error("【定时任务处理器】创建定时任务失败！{}", e.getMessage());
            CommonTools.throwUserExceptionInfo("创建定时任务失败！" + e.getMessage());
        }
    }

    /**
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) throws UserOperationIllegalException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            log.error("【定时任务处理器】修改定时任务失败！{}", e.getMessage());
            CommonTools.throwUserExceptionInfo("修改定时任务失败！" + e.getMessage());
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public void removeJob(String jobName, String jobGroupName,
                          String triggerName, String triggerGroupName) throws UserOperationIllegalException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            log.error("【定时任务处理器】移除定时任务失败！{}", e.getMessage());
            CommonTools.throwUserExceptionInfo("移除定时任务失败！" + e.getMessage());
        }
    }

    /**
     * 启动所有定时任务
     */
    public void startJobs() throws UserOperationIllegalException {
        try {
            scheduler.start();
        } catch (Exception e) {
            log.error("【定时任务处理器】启动所有定时任务失败！{}", e.getMessage());
            CommonTools.throwUserExceptionInfo("启动所有定时任务失败！" + e.getMessage());
        }
    }

    /**
     * 关闭所有定时任务
     */
    public void shutdownJobs() throws UserOperationIllegalException {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            log.error("【定时任务处理器】关闭所有定时任务失败！{}", e.getMessage());
            CommonTools.throwUserExceptionInfo("关闭所有定时任务失败！" + e.getMessage());
        }
    }
}