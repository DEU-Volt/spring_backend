package deu.se.volt.microservices.core.transaction.service;


import deu.se.volt.microservices.core.transaction.entity.Transaction;
import deu.se.volt.microservices.core.transaction.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

//    public Transaction save(Transaction transaction) {
//        retur
//    }
}
