package com.shahed.taskmanagementsystem.config;

import com.shahed.taskmanagementsystem.job.EmailSenderScheduler;
import com.shahed.taskmanagementsystem.job.UpcomingDueDateNotificationScheduler;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail upcomingDueDateNotificationJobDetail() {
        return JobBuilder.newJob(UpcomingDueDateNotificationScheduler.class)
            .withIdentity("UpcomingDueDateNotificationJob")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger upcomingDueDateNotificationJobTrigger() {
        SimpleScheduleBuilder triggerSchedule = SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(10) // Repeat every 10 seconds
            .repeatForever();

        return TriggerBuilder.newTrigger()
            .forJob(upcomingDueDateNotificationJobDetail())
            .withIdentity("UpcomingDueDateNotificationJobTrigger")
            .withSchedule(triggerSchedule)
            .build();
    }

    @Bean
    public JobDetail emailSenderSchedulerJobDetail() {
        return JobBuilder.newJob(EmailSenderScheduler.class)
            .withIdentity("EmailSenderJob")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger emailSenderSchedulerJobTrigger() {
        SimpleScheduleBuilder triggerSchedule = SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(10) // Repeat every 10 seconds
            .repeatForever();

        return TriggerBuilder.newTrigger()
            .forJob(emailSenderSchedulerJobDetail())
            .withIdentity("EmailSenderJobTrigger")
            .withSchedule(triggerSchedule)
            .build();
    }
}
