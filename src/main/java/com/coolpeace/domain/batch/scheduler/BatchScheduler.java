package com.coolpeace.domain.batch.scheduler;

import com.coolpeace.domain.batch.job.StatisticsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;

    private final StatisticsJob statisticsJob;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;
    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyJob() throws Exception {
        jobLauncher.run(statisticsJob.dailyStaticsJob(jobRepository, platformTransactionManager),
            new JobParameters());
    }

}