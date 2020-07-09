package com.casino.wallet.service;

import com.casino.wallet.dto.model.TransactionDto;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getAllTransactionsByPlayerId(Long playerId);

    List<TransactionDto> getAllTransactionsByRoundId(Long roundId);

    TransactionDto getTransaction(Long transactionId);

    TransactionDto withdraw (TransactionDto transactionDto);

    TransactionDto deposit (TransactionDto transactionDto);

    TransactionDto cancel (TransactionDto transactionDto);


}
