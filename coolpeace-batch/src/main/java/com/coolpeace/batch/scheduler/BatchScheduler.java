package com.coolpeace.batch.scheduler;

import com.coolpeace.batch.job.DailyStatisticsConfig;
import com.coolpeace.batch.job.MonthlyStatisticsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;

    private final DailyStatisticsConfig dailyStatisticsConfig;

    private final MonthlyStatisticsConfig monthlyStatisticsConfig;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Scheduled(cron = "0 0 1 * * *")
    public void runDailyJob() throws Exception {
        log.info("---------{}---------", LocalDateTime.now());
        log.info("daily scheduler start");
        JobParameters jobParameters = new JobParametersBuilder().addString("",
            LocalDateTime.now().toString()).toJobParameters();
        jobLauncher.run(
            dailyStatisticsConfig.dailyStatisticsJob(jobRepository, platformTransactionManager),
            jobParameters);
    }
    @Scheduled(cron = "0 0 0 10 * *")
    public void runMonthlyJob() throws Exception {
        log.info("---------{}---------", LocalDateTime.now());
        log.info("monthly scheduler start");
        JobParameters jobParameters = new JobParametersBuilder().addString("",
            LocalDateTime.now().toString()).toJobParameters();
        jobLauncher.run(
            monthlyStatisticsConfig.monthlyStatisticsJob(jobRepository, platformTransactionManager),
            jobParameters);
    }


}
