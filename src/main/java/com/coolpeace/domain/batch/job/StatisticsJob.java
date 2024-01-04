package com.coolpeace.domain.batch.job;

import com.coolpeace.domain.batch.tasklet.SettlementTasklet;
import com.coolpeace.domain.statistics.service.DailyStatisticsService;
import com.coolpeace.domain.batch.tasklet.CouponTasklet;
import com.coolpeace.domain.batch.tasklet.ReservationTasklet;
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
public class StatisticsJob {

    private final DailyStatisticsService dailyStatisticsService;

    @Bean(name = "dailyStaticsJob")
    public Job dailyStaticsJob(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) throws Exception {
        return new JobBuilder("dailyStaticsJob", jobRepository)
            .start(saleStep(jobRepository, platformTransactionManager))
            .next(couponStep(jobRepository, platformTransactionManager))
            .next(settlementStep(jobRepository, platformTransactionManager))
            .build();
    }

    @Bean
    public Step saleStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) throws  Exception{
        return new StepBuilder("saleStep", jobRepository)
            .tasklet(new ReservationTasklet(dailyStatisticsService),platformTransactionManager)
            .build();
    }

    @Bean
    public Step couponStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("couponStep", jobRepository)
            .tasklet(new CouponTasklet(dailyStatisticsService),platformTransactionManager)
            .build();
    }

    @Bean
    public Step settlementStep(JobRepository jobRepository,
        PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("settlementStep", jobRepository)
            .tasklet(new SettlementTasklet(dailyStatisticsService), platformTransactionManager)
            .build();
    }


}
