package io.springbatchs.demo.step;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepConfiguration {

    //step 흐름 볼려고 생성

    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2){
        return new JobBuilder("job",jobRepository)
                .start(step1)
                .next(step2)
                .build();
    }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder("step1",jobRepository)
                .tasklet((contribution,chunkContext)->{
                    System.out.println("step1 was executed");
                    return RepeatStatus.FINISHED;
                },tx).build();
    }


    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder("step2",jobRepository)
                .tasklet((contribution,chunkContext)->{
                    System.out.println("step2 was executed");
                    return RepeatStatus.FINISHED;
                },tx).build();
    }


}
