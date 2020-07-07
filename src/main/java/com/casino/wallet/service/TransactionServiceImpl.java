package com.casino.wallet.service;

import com.casino.wallet.dto.mapper.TransactionMapper;
import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.exception.EntityType;
import com.casino.wallet.exception.ExceptionType;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.model.player.Player;
import com.casino.wallet.model.transaction.Transaction;
import com.casino.wallet.repository.AccountRepository;
import com.casino.wallet.repository.PlayerRepository;
import com.casino.wallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.casino.wallet.exception.EntityType.*;
import static com.casino.wallet.exception.ExceptionType.*;

@Component
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PlayerRepository playerRepository;

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
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isPresent()) {
            return StreamSupport.stream(transactionRepository.findByPlayerId(playerId).spliterator(), false)
                    .map(TransactionMapper::toTransactionDto)
                    .collect(Collectors.toList());
        }
        throw exception(PLAYER,ENTITY_NOT_FOUND,playerId.toString());
    }

    @Override
    public List<TransactionDto> getAllTransactionsByRoundId(Long roundId) {
        return StreamSupport.stream(transactionRepository.findByRoundId(roundId).spliterator(), false)
                .map(TransactionMapper::toTransactionDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getTransaction(Long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionId);
        if (transaction.isPresent()){
            return TransactionMapper.toTransactionDto(transaction.get());
        }
        throw exception(TRANSACTION,ENTITY_NOT_FOUND,transactionId.toString());
    }

    @Override
    @Transactional
    public TransactionDto withdraw(TransactionDto transactionDto) {
        Optional<Player> player = playerRepository.findById(transactionDto.getPlayerId());
        if(player.isPresent()){
            Optional<Account> account = accountRepository.findById(transactionDto.getPlayerId());
            if (account.isPresent()){
                Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionDto.getTransactionId());
                if (transaction.isPresent()){
                    if(isSameTransaction(transactionDto,transaction.get())){
                        return TransactionMapper.toTransactionDto(transaction.get());
                    }
                    throw exception(TRANSACTION,DUPLICATE_ENTITY, String.valueOf(transactionDto.getTransactionId()));
                } else {
                    if(hasEnoughFunds(transactionDto,account.get())){
                        Account accountAfterTransaction = new Account()
                                .setAmount(account.get().getAmount() - transactionDto.getAmount())
                                .setId(transactionDto.getPlayerId());
                        Transaction newTransaction = new Transaction()
                                .setTransactionId(transactionDto.getTransactionId())
                                .setAmount(transactionDto.getAmount())
                                .setPlayerId(transactionDto.getPlayerId())
                                .setRoundId(transactionDto.getRoundId())
                                .setWithdrawal(true)
                                .setCancelled(false);

                        accountRepository.save(accountAfterTransaction);
                        return TransactionMapper.toTransactionDto(transactionRepository.save(newTransaction));
                    }
                    throw exception(TRANSACTION,NOT_ENOUGH_FUNDS,
                            String.valueOf(transactionDto.getPlayerId()),
                            String.valueOf(transactionDto.getAmount()),
                            String.valueOf(account.get().getAmount()));
                }
            }
            throw exception(ACCOUNT,ENTITY_NOT_FOUND,String.valueOf(transactionDto.getPlayerId()));
        }
        throw exception(PLAYER,ENTITY_NOT_FOUND,String.valueOf(transactionDto.getPlayerId()));
    }

    @Override
    @Transactional
    public TransactionDto deposit(TransactionDto transactionDto) {
        Optional<Player> player = playerRepository.findById(transactionDto.getPlayerId());
        if (player.isPresent()) {
            Optional<Account> account = accountRepository.findById(transactionDto.getPlayerId());
            if (account.isPresent()) {
                Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionDto.getTransactionId());
                if (transaction.isPresent()) {
                    if(isSameTransaction(transactionDto,transaction.get())){
                        return TransactionMapper.toTransactionDto(transaction.get());
                    }
                    throw exception(TRANSACTION,DUPLICATE_ENTITY, String.valueOf(transactionDto.getTransactionId()));
                } else {

                    Account accountAfterTransaction = new Account()
                            .setAmount(account.get().getAmount() + transactionDto.getAmount())
                            .setId(transactionDto.getPlayerId());
                    Transaction newTransaction = new Transaction()
                            .setTransactionId(transactionDto.getTransactionId())
                            .setAmount(transactionDto.getAmount())
                            .setPlayerId(transactionDto.getPlayerId())
                            .setRoundId(transactionDto.getRoundId())
                            .setWithdrawal(true)
                            .setCancelled(false);

                    accountRepository.save(accountAfterTransaction);
                    return TransactionMapper.toTransactionDto(transactionRepository.save(newTransaction));

                }
            }
            throw exception(ACCOUNT,ENTITY_NOT_FOUND,String.valueOf(transactionDto.getPlayerId()));
        }
        throw exception(PLAYER,ENTITY_NOT_FOUND,String.valueOf(transactionDto.getPlayerId()));
    }

    @Override
    @Transactional
    public TransactionDto cancel(TransactionDto transactionDto) {
        Optional<Player> player = playerRepository.findById(transactionDto.getPlayerId());
        if (player.isPresent()) {
            Optional<Account> account = accountRepository.findById(transactionDto.getPlayerId());
            if (account.isPresent()) {
                    Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionDto.getTransactionId());
                    if (transaction.isPresent()) {
                        if(transaction.get().isWithdrawal()){
                            if(isSameTransaction(transactionDto,transaction.get())) {

                            Account accountAfterTransaction = new Account()
                                    .setAmount(account.get().getAmount() - transactionDto.getAmount())
                                    .setId(transactionDto.getPlayerId());
                            Transaction newTransaction = new Transaction()
                                    .setTransactionId(transactionDto.getTransactionId())
                                    .setAmount(transactionDto.getAmount())
                                    .setPlayerId(transactionDto.getPlayerId())
                                    .setRoundId(transactionDto.getRoundId())
                                    .setWithdrawal(true)
                                    .setCancelled(true);

                            accountRepository.save(accountAfterTransaction);
                            return TransactionMapper.toTransactionDto(transactionRepository.save(newTransaction));
                        }
                        throw exception(TRANSACTION,NOT_ALLOWED, "Cancelling a transaction with non matching values");
                        }
                        throw exception(TRANSACTION,NOT_ALLOWED,"Cancelling a deposit transaction");
                    } else {

                        Transaction newTransaction = new Transaction()
                                .setTransactionId(transactionDto.getTransactionId())
                                .setAmount(transactionDto.getAmount())
                                .setPlayerId(transactionDto.getPlayerId())
                                .setRoundId(transactionDto.getRoundId())
                                .setWithdrawal(true)
                                .setCancelled(true);

                        return TransactionMapper.toTransactionDto(transactionRepository.save(newTransaction));
                    }
            }
            throw exception(ACCOUNT,ENTITY_NOT_FOUND,String.valueOf(transactionDto.getPlayerId()));
        }
        throw exception(PLAYER,ENTITY_NOT_FOUND,String.valueOf(transactionDto.getPlayerId()));
    }

    // Make sure the player has enough funds in his account.
    // Removing the transaction amount from the account amount and then checking if the resulting double is not negative.
    private boolean hasEnoughFunds(TransactionDto inputTransaction, Account account){
        double accountAfterTransaction = account.getAmount() - inputTransaction.getAmount();
        return !(Double.compare(accountAfterTransaction,0.0) < 0);
    }

    //Checking if the two transactions are identical on input level,
    //if they are we can assume its a duplicate package and should return OK.
    private boolean isSameTransaction(TransactionDto inputTransaction, Transaction oldTransaction){
        return (inputTransaction.getAmount() == oldTransaction.getAmount() &&
                inputTransaction.getPlayerId() == oldTransaction.getPlayerId() &&
                inputTransaction.getRoundId() == oldTransaction.getRoundId());
    }

    private RuntimeException exception (EntityType entityType, ExceptionType exceptionType, String... args){
        return WalletException.throwException(entityType,exceptionType,args);
    }
}
