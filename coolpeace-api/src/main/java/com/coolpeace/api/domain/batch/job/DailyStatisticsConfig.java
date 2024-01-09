package com.coolpeace.api.domain.batch.job;

import com.coolpeace.api.domain.batch.tasklet.SettlementTasklet;
import com.coolpeace.api.domain.statistics.service.DailyStatisticsService;
import com.coolpeace.api.domain.batch.tasklet.CouponTasklet;
import com.coolpeace.api.domain.batch.tasklet.ReservationTasklet;
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
public class DailyStatisticsConfig {

    private final DailyStatisticsService dailyStatisticsService;

    private final CustomStepListener customStepListener;

    
    @Bean(name = "dailyStatisticsJob")
    public Job dailyStatisticsJob(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) throws Exception {
        log.info("일간 통계 집계 시작");
        return new JobBuilder("dailyStatisticsJob", jobRepository)
            .start(saleStep(jobRepository, platformTransactionManager))
            .next(couponStep(jobRepository, platformTransactionManager))
            .next(settlementStep(jobRepository, platformTransactionManager))
            .build();
    }
    

    
    @Bean
    public Step saleStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) throws  Exception{
        log.info("sale step start");
        return new StepBuilder("saleStep", jobRepository)
            .tasklet(new ReservationTasklet(dailyStatisticsService),platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

    @Bean
    public Step couponStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("coupon step start");
        return new StepBuilder("couponStep", jobRepository)
            .tasklet(new CouponTasklet(dailyStatisticsService),platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

    @Bean
    public Step settlementStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        log.info("settlement step start");
        return new StepBuilder("settlementStep", jobRepository)
            .tasklet(new SettlementTasklet(dailyStatisticsService), platformTransactionManager)
            .listener(customStepListener)
            .build();
    }

}
