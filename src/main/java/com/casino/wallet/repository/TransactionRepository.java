package com.casino.wallet.repository;

import com.casino.wallet.model.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByPlayerId(Long playerId);
    List<Transaction> findByRoundId(Long roundId);
    Optional<Transaction> findByTransactionId(Long transactionId);
}
