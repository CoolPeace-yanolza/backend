package com.coolpeace.batch.job;

import com.coolpeace.batch.tasklet.CouponDownloadTop3Tasklet;
import com.coolpeace.batch.tasklet.LocalCouponDownloadTasklet;
import com.coolpeace.batch.tasklet.MonthlySumTasklet;
import com.coolpeace.batch.service.MonthlyStatisticsService;
import com.coolpeace.batch.tasklet.CompleteSettlementTasklet;
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
            .start(completeSettlementStep(jobRepository,platformTransactionManager))
            .next(monthlySumStep(jobRepository, platformTransactionManager))
            .next(localCouponDownloadStep(jobRepository, platformTransactionManager))
            .next(couponDownloadTop3Step(jobRepository,platformTransactionManager))
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
    public Step completeSettlementStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("completeSettlement step start");
        return new StepBuilder("completeSettlementStep", jobRepository)
            .tasklet(new CompleteSettlementTasklet(monthlyStatisticsService),
                platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

}
