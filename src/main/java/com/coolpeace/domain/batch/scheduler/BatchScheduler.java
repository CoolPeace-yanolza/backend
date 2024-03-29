package com.coolpeace.domain.batch.scheduler;

import com.coolpeace.domain.batch.job.DailyStatisticsConfig;
import com.coolpeace.domain.batch.job.MonthlyStatisticsConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

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
    @Scheduled(cron = "0 0 0 1 * *")
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
