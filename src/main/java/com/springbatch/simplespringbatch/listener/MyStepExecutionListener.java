package com.springbatch.simplespringbatch.listener;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;

public class MyStepExecutionListener implements StepExecutionListener {
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
    @Override
    public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("MyStepExecutionListener afterStep");
        return new ExitStatus("TEST_STATUS");
    }

    /**
     * Initialize the state of the listener with the {@link StepExecution} from the
     * current scope.
     *
     * @param stepExecution instance of {@link StepExecution}.
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
    }
}
