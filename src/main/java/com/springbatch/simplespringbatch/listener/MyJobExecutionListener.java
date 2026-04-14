package com.springbatch.simplespringbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyJobExecutionListener {
    /**
     * Callback before a job executes.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        log.info("JobExecutionListener:BeforeJob - Job name {} \n\t Parameters: {} - Start: {}",  jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters(),  jobExecution.getStartTime());


        jobExecution.getExecutionContext().put("sk1", "XYZ");
    }

    /**
     * Callback after completion of a job. Called after both successful and failed
     * executions. To perform logic on a particular status, use
     * {@code if (jobExecution.getStatus() == BatchStatus.X)}.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        log.info("JobExecutionListener:AfterJob - Job name {} \n\t Start {} - End {}",  jobExecution.getJobInstance().getJobName(), jobExecution.getStartTime(), jobExecution.getEndTime());
    }
}
