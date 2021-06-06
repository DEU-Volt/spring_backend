package deu.se.volt.microservices.core.transaction.jobs;

import deu.se.volt.microservices.core.transaction.repository.StatisticsRepository;
import deu.se.volt.microservices.core.transaction.service.TransactionService;
import deu.se.volt.microservices.core.transaction.tasklets.DailyStatisticsTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DailyStatisticsConfig {
    private final JobBuilderFactory jobBuilderFactory; // Job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; // Step 빌더 생성용
    private final TransactionService transactionService;
    private final StatisticsRepository statisticsRepository;

    // JobBuilderFactory를 통해서 tutorialJob을 생성

    @Bean
    public Job dailyStatisticsJob() {
        return jobBuilderFactory.get("dailyStatisticsJob")
//                .preventRestart()
                .start(dailyStatisticsStep())  // Step 설정
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
    @Bean
    @JobScope
    public Step dailyStatisticsStep() {
        return stepBuilderFactory.get("dailyStatisticsStep")
                .tasklet(dailyStatisticsTasklet(transactionService,statisticsRepository)) // Tasklet 설정
                .build();
    }

    @Bean
    public Tasklet dailyStatisticsTasklet(TransactionService transactionService, StatisticsRepository statisticsRepository) {
        return new DailyStatisticsTasklet(transactionService, statisticsRepository);
    }

}
