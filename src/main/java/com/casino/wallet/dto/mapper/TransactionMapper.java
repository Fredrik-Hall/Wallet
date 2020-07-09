package com.casino.wallet.dto.mapper;

import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.model.transaction.Transaction;

public class TransactionMapper {
    public static TransactionDto toTransactionDto(Transaction transaction){
        return new TransactionDto()
                .setAmount(transaction.getAmount())
                .setCreated(transaction.getCreated())
                .setId(transaction.getId())
                .setPlayerId(transaction.getPlayerId())
                .setRoundId(transaction.getRoundId())
                .setTransactionId(transaction.getTransactionId())
                .setWithdrawal(transaction.isWithdrawal())
                .setCancelled(transaction.isCancelled());
    }
}
