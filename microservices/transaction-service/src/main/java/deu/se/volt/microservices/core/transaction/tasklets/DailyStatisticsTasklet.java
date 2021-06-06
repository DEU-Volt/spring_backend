package deu.se.volt.microservices.core.transaction.tasklets;


import deu.se.volt.microservices.core.transaction.entity.DailyStatistics;
import deu.se.volt.microservices.core.transaction.entity.Transaction;
import deu.se.volt.microservices.core.transaction.repository.StatisticsRepository;
import deu.se.volt.microservices.core.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@StepScope
public class DailyStatisticsTasklet implements Tasklet, StepExecutionListener {

    private final TransactionService transactionService;
    private final StatisticsRepository statisticsRepository;
    Map<String, List<Transaction>> groupingMap;

    @Autowired
    public DailyStatisticsTasklet(TransactionService transactionService, StatisticsRepository statisticsRepository) {

        this.transactionService = transactionService;
        this.statisticsRepository = statisticsRepository;
         }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        for (String key : groupingMap.keySet()) {
            IntSummaryStatistics summaryStatistics = groupingMap.get(key).stream().collect(Collectors.summarizingInt(Transaction::getTransactionPrice));
            var dailyStatistics = DailyStatistics.builder()
                    .highestPrice(summaryStatistics.getMax())
                    .lowestPrice(summaryStatistics.getMin())
                    .avgPrice(summaryStatistics.getAverage())
                    .localDate(groupingMap.get(key).get(0).getCreatedAt().toLocalDate())
                    .modelName(key)
                    .build();
            statisticsRepository.save(dailyStatistics);
        }
        log.info("DailyStatisticsTasklet : execute success");
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        List<Transaction> transactionList = transactionService.loadYesterdayAllTransactions();
        groupingMap = transactionList.stream().collect(Collectors.groupingBy(Transaction::getModelName));
        log.info("DailyStatisticsTasklet : beforeStep");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("DailyStatisticsTasklet : afterStep");

        return ExitStatus.COMPLETED;
    }
}
