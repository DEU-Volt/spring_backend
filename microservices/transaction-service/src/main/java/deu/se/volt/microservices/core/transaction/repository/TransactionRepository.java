package deu.se.volt.microservices.core.transaction.repository;

import deu.se.volt.microservices.core.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionsByModelName(String modelName);
}
