package deu.se.volt.microservices.core.transaction.repository;

import deu.se.volt.microservices.core.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionsByModelName(String modelName);

    List<Transaction> findTransactionsByCreatedAtBetweenOrderByModelNameAscCreatedAtAsc(LocalDateTime start, LocalDateTime end);

    List<Transaction> findTransactionsByBuyer(String buyer);

    List<Transaction> findTransactionsBySeller(String seller);

    List<Transaction> findTransactionsByBuyerOrSeller(String userName, String userName2);
}
