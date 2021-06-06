package deu.se.volt.microservices.core.transaction.service;


import deu.se.volt.microservices.core.transaction.entity.Transaction;
import deu.se.volt.microservices.core.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> loadYesterdayAllTransactions() {
        log.info("loadYesterdayAllTransactions : called");
        return transactionRepository.findTransactionsByCreatedAtBetweenOrderByModelNameAscCreatedAtAsc
                (LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0))
                        ,LocalDateTime.of(LocalDate.now().minusDays(1),LocalTime.of(23,59)));
    }



}

