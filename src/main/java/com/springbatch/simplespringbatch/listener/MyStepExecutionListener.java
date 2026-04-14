package com.springbatch.simplespringbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyStepExecutionListener /*implements StepExecutionListener*/ {
    /**
     * Give a listener a chance to modify the exit status from a step. The value returned
     * is combined with the normal exit status by using
     * {@link ExitStatus#and(ExitStatus)}.
     * <p>
     * Called after execution of the step's processing logic (whether successful or
     * failed). Throwing an exception in this method has no effect, as it is only logged.
     *
     * @param stepExecution a {@link StepExecution} instance.
     * @return an {@link ExitStatus} to combine with the normal value. Return {@code null}
     * (the default) to leave the old value unchanged.
     */
    @BeforeStep
    public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
        log.info("MyStepExecutionListener:afterStep - Step name: {}", stepExecution.getStepName());
        log.info("Start time: {}. End time: {}", stepExecution.getStartTime(), stepExecution.getEndTime());
        // using null => the step ExitStatus will depend on the default behavior => Success -> COMPLETED
        return null;
    }

    /**
     * Initialize the state of the listener with the {@link StepExecution} from the
     * current scope.
     *
     * @param stepExecution instance of {@link StepExecution}.
     */
    @AfterStep
    public void beforeStep(StepExecution stepExecution) {
        log.info("MyStepExecutionListener:beforeStep - Step name: {}", stepExecution.getStepName());
        log.info("Start time: {}", stepExecution.getStartTime());
    }
}
