package com.coolpeace.domain.batch.tasklet;

import com.coolpeace.domain.statistics.service.DailyStatisticsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class CouponTasklet implements Tasklet, StepExecutionListener {

    private final DailyStatisticsService dailyStatisticsService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {

        dailyStatisticsService.updateCoupon();

        return RepeatStatus.FINISHED;
    }


}
