package io.springbatchs.demo.jobExecution;

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
public class JobExecutionConfiguration {

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

// job_execution에서 status가 최종적으로  completed 로끝나면 job 실행이 안된다
// 대신 status가 faild 일경우 계속 실행이 되고 그때마다 jobexecution개체가 생성되고 테이블에 저장이 된다     
    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder("step2",jobRepository)
                .tasklet((contribution,chunkContext)->{
                    System.out.println("step2 was executed");
//                    throw new RuntimeException("step2 has failed");
                    return RepeatStatus.FINISHED;
                },tx).build();
    }


}
