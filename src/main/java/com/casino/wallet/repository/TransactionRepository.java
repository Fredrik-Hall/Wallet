package com.casino.wallet.repository;

import com.casino.wallet.model.wallet.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Transaction findByplayerId(int playerId);
}
