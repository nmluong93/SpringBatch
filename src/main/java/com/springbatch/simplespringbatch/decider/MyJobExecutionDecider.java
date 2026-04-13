package com.springbatch.simplespringbatch.decider;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.step.StepExecution;

public class MyJobExecutionDecider implements JobExecutionDecider {
    /**
     * Strategy for branching an execution based on the state of an ongoing
     * {@link JobExecution}. The return value will be used as a status to determine the
     * next step in the job.
     *
     * @param jobExecution  a job execution
     * @param stepExecution the latest step execution (may be {@code null})
     * @return the exit status code
     */
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, @Nullable StepExecution stepExecution) {
        System.out.println("MyJobExecutionDecider");
        return new FlowExecutionStatus("FLOW_TEST_STATUS");
    }
}
