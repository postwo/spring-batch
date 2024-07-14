package io.springbatchs.demo.batch1;

import lombok.RequiredArgsConstructor;
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
public class HelloJobConfiguration {

    //스프링 배치5,스프링3.xx  부터는 설정부분이 달라졌기 때문에 이방식으로 하기

    @Bean
    public Job helloJob(JobRepository jobRepository, Step helloStep1, Step helloStep2) {
        return new JobBuilder("helloJob", jobRepository)// job의 이름이다 = job을 생성
                .start(helloStep1)
                .next(helloStep2)
                .build();
    }

//    /*PlatformTransactionManager은 스프링 프레임워크에서 트랜잭션 관리를 추상화한 인터페이스입니다.
//     이 인터페이스는 스프링에서 트랜잭션을 시작, 커밋, 롤백하는 등의 트랜잭션 관리 기능을 제공합*/
//    /*
//     PlatformTransactionManager를 사용하지 않고 트랜잭션을 빼면 왜 동작하지 않는지 이해하는 데 도움이 될 것입니다.
//    일반적으로 스프링 배치에서 각 스텝은 트랜잭션 내에서 실행됩니다. 그렇기 때문에 스프링 배치의 각 단계에는 트랜잭션 관리자가 필요합니다.
//    주어진 코드에서 helloStep1 메서드는 StepBuilder를 사용하여 스텝을 생성하고, 해당 스텝에서 트랜잭션을 관리하기 위해
//    PlatformTransactionManager를 사용합니다. 만약 트랜잭션 관리자를 제공하지 않으면 스프링 배치는 트랜잭션을 시작할
//    수 없으며 따라서 스텝이 실행되지 않습니다.
//    따라서 트랜잭션 관리자가 필요한 이유는 각 스텝이 데이터베이스 작업을 수행할 때 데이터의 일관성을
//    보장하고 롤백할 수 있도록 하는 것입니다.*/


    //중요
    //PlatformTransactionManager은  인터페이스이고 트랜잭션을 관리한다
    //tasklet을 하는 도중 예외가 발새하면 rollback을 하고 예외가 발생 안하고 모든작업을 통과 하면 commit을한다

    /*contribution = StepContribution => 현재 스텝의 상태 및 진행 상황을 나타내는 객체입니다. 주로 스텝의 실행 결과를 추적하고,
    스텝 간의 데이터 교환을 위해 사용됩니다.
    상태 추적: StepContribution은 스텝의 상태, 예를 들어 읽은 아이템 수, 작성한 아이템 수, 필터링된 아이템 수 등을 추적합니다.
    상태 업데이트: 예를 들어, 예외가 발생했을 때 상태를 업데이트할 수 있습니다.
    통계 제공: 스텝 실행 후 통계 정보를 제공할 수 있습니다
    */

    /*ChunkContext => 현재 청크의 실행 컨텍스트를 나타냅니다. 이는 JobExecution과 StepExecution의 컨텍스트와 유사하지만,
    청크 단위로 데이터를 저장하고 공유하는 데 중점을 둡니다.
    청크 수준의 컨텍스트: 각 청크 실행 간의 공유 데이터를 저장합니다.
    데이터 저장 및 조회: 청크 내에서 데이터를 저장하거나 조회할 수 있습니다.
    파라미터 접근: JobParameters와 StepExecutionContext에 접근할 수 있습니다.
    * */

    //tasklet => 기본적으로 무한 반복한다 

    @Bean
    public Step helloStep1(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "helloStep1", jobRepository) // step 생성
                .tasklet((contribution, chunkContext) -> {  //tasklet 무한반복 시킨다
//                    System.out.println("====================================");
//                    System.out.println(" helloStep1 executed ");
//                    System.out.println("====================================");
                    return RepeatStatus.FINISHED; //tasklet의 기능을 한번만 실행시키고 종료시킨다
                }, tx).build();
    }

    @Bean
    public Step helloStep2(JobRepository jobRepository, PlatformTransactionManager tx) {
        return new StepBuilder( "helloStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
//                    System.out.println("====================================");
//                    System.out.println(" helloStep2 executed ");
//                    System.out.println("====================================");
                    return RepeatStatus.FINISHED;
                }, tx).build();
    }


}   
