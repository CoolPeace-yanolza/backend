package com.coolpeace.domain.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomStepListener implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if ( stepExecution.getExitStatus().equals(ExitStatus.FAILED)){
            log.info("Step 도중 예외 발생 ");
            return ExitStatus.COMPLETED;
        }
        return StepExecutionListener.super.afterStep(stepExecution);
    }
}
