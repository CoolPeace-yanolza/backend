package com.coolpeace.domain.batch.job;

import com.coolpeace.domain.batch.tasklet.CouponDownloadTop3Tasklet;
import com.coolpeace.domain.batch.tasklet.LocalCouponAvgTasklet;
import com.coolpeace.domain.batch.tasklet.LocalCouponDownloadTasklet;
import com.coolpeace.domain.batch.tasklet.MonthlySumTasklet;
import com.coolpeace.domain.statistics.service.MonthlyStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MonthlyStatisticsConfig {

    private final MonthlyStatisticsService monthlyStatisticsService;
    private final CustomStepListener customStepListener;

    @Bean(name = "monthlyStatisticsJob")
    public Job monthlyStatisticsJob(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("월간 통계 집계 시작");
        return new JobBuilder("monthlyStatisticsJob", jobRepository)
            .start(monthlySumStep(jobRepository, platformTransactionManager))
            .next(localCouponDownloadStep(jobRepository, platformTransactionManager))
            .next(couponDownloadTop3Step(jobRepository,platformTransactionManager))
            .next(localCouponAvgStep(jobRepository,platformTransactionManager))
            .build();
    }

    @Bean
    public Step monthlySumStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("monthlySum step start");
        return new StepBuilder("monthlySumStep", jobRepository)
            .tasklet(new MonthlySumTasklet(monthlyStatisticsService), platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

    @Bean
    public Step localCouponDownloadStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("localCouponDownload step start");
        return new StepBuilder("localCouponDownloadStep", jobRepository)
            .tasklet(new LocalCouponDownloadTasklet(monthlyStatisticsService),
                platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

    @Bean
    public Step couponDownloadTop3Step(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("getCouponDownloadTop3 step start");
        return new StepBuilder("CouponDownloadTop3Step", jobRepository)
            .tasklet(new CouponDownloadTop3Tasklet(monthlyStatisticsService),
                platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

    @Bean
    public Step localCouponAvgStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("LocalCouponAvg step start");
        return new StepBuilder("localCouponAvgStep", jobRepository)
            .tasklet(new LocalCouponAvgTasklet(monthlyStatisticsService),
                platformTransactionManager)
            .listener(customStepListener)
            .build();
    }
}
