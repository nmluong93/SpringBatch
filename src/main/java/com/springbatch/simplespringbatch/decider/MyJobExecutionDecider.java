package com.springbatch.simplespringbatch.decider;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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
        log.info("JobExecution decide start");
        String sk1 = jobExecution.getExecutionContext().getString("sk1");
        String sk2 = jobExecution.getExecutionContext().getString("sk2");
        String existStatus = "STEP_5";
        if ("ABC".equals(sk1) && "KLM".equals(sk2)) {
            existStatus = "STEP_3";
        } else if ("ABC".equals(sk1) && "TUV".equals(sk2)) {
            existStatus = "STEP_4";
        }
        return new FlowExecutionStatus(existStatus);
    }
}
