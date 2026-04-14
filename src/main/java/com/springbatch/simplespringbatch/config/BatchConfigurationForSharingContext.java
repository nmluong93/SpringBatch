package com.springbatch.simplespringbatch.config;

import com.springbatch.simplespringbatch.listener.MyJobExecutionListener;
import com.springbatch.simplespringbatch.listener.MyStepExecutionListener;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BatchConfigurationForSharingContext {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MyJobExecutionListener myJobExecutionListener;

    @Autowired
    private MyStepExecutionListener myStepExecutionListener;


    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new Tasklet() {

                    @Override
                    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("Step1 executed!");
                        ExecutionContext jobExecutionCtx = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                        log.info("Job execution context: {}", jobExecutionCtx);

                        jobExecutionCtx.put("sk-step1", "ABC");

                        ExecutionContext stepExtContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                        // this will promote the key-value below into Job execution context so that other steps can see it from JobExecutionContext as well
                        stepExtContext.put("k1", "ABC");
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("Task of step 2 execute");
                        ExecutionContext jobExecutionCtx = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                        log.info("Job execution context: {}", jobExecutionCtx);
//                        jobExecutionCtx.put("sk-step2", "EFD");

                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("Task of step 3 execute");
                        ExecutionContext jobExecutionCtx = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                        log.info("Job execution context: {}", jobExecutionCtx);
                        jobExecutionCtx.put("sk-step3", "ABC");
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(myStepExecutionListener)
                .build();
    }

    @Bean
    public Job firstJob() {
        return new JobBuilder("jobWithSharingDataBetweenSteps", jobRepository)
                .listener(myJobExecutionListener)
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }


    @Bean
    public StepExecutionListener promotionListener() {
        var promotionListener = new ExecutionContextPromotionListener();
        promotionListener.setKeys(new String[]{"k1", "k2"});
        return promotionListener;
    }

}
