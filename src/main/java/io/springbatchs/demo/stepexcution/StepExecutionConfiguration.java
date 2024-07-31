package io.springbatchs.demo.stepexcution;

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
public class StepExecutionConfiguration {

    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2, Step step3){
        return new JobBuilder("job",jobRepository)
                .start(step1)
                .next(step2)
                .next(step3)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager tx){
        return new StepBuilder("step1",jobRepository)
                .tasklet((contribution,chunkContext)->{
                    System.out.println("step1 has executed");
                    return RepeatStatus.FINISHED;
                },tx).build();
    }

    //재시작을 하면 성공한 step은 실행하지 않고 실패한 step2와 시도를 안한 step3가 시작된다
    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager tx){
        return new StepBuilder("step2",jobRepository)
                .tasklet((contribution,chunkContext)->{
                    System.out.println("step2 has executed");
//                    throw new RuntimeException("step2 has faild");
                    return RepeatStatus.FINISHED;
                },tx).build();
    }


    // stop2 에서 실패 했기 때문에 step3는 저장이 안된다 딱 step2까지 저장된다
    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager tx){
        return new StepBuilder("step3",jobRepository)
                .tasklet((contribution,chunkContext)->{
                    System.out.println("step3 has executed");
                    return RepeatStatus.FINISHED;
                },tx).build();
    }

}

