package com.casino.wallet.service;

import com.casino.wallet.dto.mapper.TransactionMapper;
import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.exception.EntityType;
import com.casino.wallet.exception.ExceptionType;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.repository.AccountRepository;
import com.casino.wallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<TransactionDto> getAllTransactions() {
        return StreamSupport.stream(transactionRepository.findAll().spliterator(), false)
                .map(TransactionMapper::toTransactionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getAllTransactionsByPlayerId(Long playerId) {
        return null;
    }

    @Override
    public List<TransactionDto> getAllTransactionsByRoundId(Long roundId) {
        return null;
    }

    @Override
    public TransactionDto getTransaction(Long transactionId) {
        return null;
    }

    @Override
    public TransactionDto withdraw(TransactionDto transactionDto) {
        return null;
    }

    @Override
    public TransactionDto deposit(TransactionDto transactionDto) {
        return null;
    }

    @Override
    public TransactionDto cancel(TransactionDto transactionDto) {
        return null;
    }

    private RuntimeException exception (EntityType entityType, ExceptionType exceptionType, String... args){
        return WalletException.throwException(entityType,exceptionType,args);
    }
}
