package com.springbatch.simplespringbatch.config;

import com.springbatch.simplespringbatch.decider.MyJobExecutionDecider;
import com.springbatch.simplespringbatch.listener.MyStepExecutionListener;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;

    private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

    @Bean
    public StepExecutionListener myStepExecutionListener() {
        return new MyStepExecutionListener();
    }

    @Bean
    public JobExecutionDecider myJobExecutionDecider() {
        return new MyJobExecutionDecider();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new CustomizedTaskLet("Task of step 1 execute"))
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        boolean failed = false;
                        if (failed) {
                            throw new Exception("failed");
                        }
                        log.info("Task of step 2 execute");
                        return RepeatStatus.FINISHED;
                    }
                })
//                .listener(myStepExecutionListener())
                .build();
    }

    @Bean
    public Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet(new CustomizedTaskLet("Task of step 3 execute"))
                .build();
    }

    @Bean
    public Step step4() {
        return new StepBuilder("step4", jobRepository)
                .tasklet(new CustomizedTaskLet("Task of step 4 execute"))
                .build();
    }

    @Bean
    public Job firstJob(JobRepository jobRepository) {
        return new JobBuilder("simpleJobWithNextTransition", jobRepository)
//                .preventRestart()
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Job secondJob(JobRepository jobRepository) {
        return new JobBuilder("simpleJobWithOnFromToTransition", jobRepository)
                .start(step1())
                .on("COMPLETED")
                .to(step2())
                .from(step2())
                .on("COMPLETED")
                .to(step3())
                .end()
                .build();
    }

    @Bean
    public Job thirdJob(JobRepository jobRepository) {
        return new JobBuilder("simpleJobWithOnFromToTransition2", jobRepository)
                .start(step1())
                .on("COMPLETED")
                .to(step2())
                .from(step2())
                .on("TEST_STATUS")
                .to(step3())
                .from(step2())
                .on("FAILED")
                .to(step4())
                .end()
                .build();
    }


    @Bean
    public Job job4(JobRepository jobRepository) {
        return new JobBuilder("testJobExecutionDecider", jobRepository)
                .start(step1())
                    .on("COMPLETED")
                .to(myJobExecutionDecider())
                    .on("FLOW_TEST_STATUS")
                .to(step2())
                .from(myJobExecutionDecider())
                    .on("*")
                .to(step3())
                .end()
                .build();
    }


    public static class CustomizedTaskLet implements Tasklet {
        private final String stepInfo;

        public CustomizedTaskLet(String logMsg) {
            this.stepInfo = logMsg;
        }

        @Override
        public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            log.info("Executing: " + stepInfo);
            return RepeatStatus.FINISHED;
        }
    }
}
