package com.casino.wallet.service;

import com.casino.wallet.dto.mapper.AccountMapper;
import com.casino.wallet.dto.model.AccountDto;
import com.casino.wallet.exception.EntityType;
import com.casino.wallet.exception.ExceptionType;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.casino.wallet.exception.EntityType.ACCOUNT;
import static com.casino.wallet.exception.ExceptionType.ENTITY_NOT_FOUND;

@Component
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountDto getAccountByPlayerId(Long playerId) {
        Optional<Account> account = accountRepository.findById(playerId);
        if (account.isPresent()){
            return AccountMapper.toAccountDto(account.get());
        }
        throw exception(ACCOUNT,ENTITY_NOT_FOUND,playerId.toString());
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Optional<Account> accountById = accountRepository.findById(accountDto.getId());
        if (accountById.isPresent()){
            Account updatedAccount = new Account()
                    .setAmount(accountDto.getAmount())
                    .setId(accountDto.getId());
            return AccountMapper.toAccountDto(accountRepository.save(updatedAccount));
        }
        throw exception(ACCOUNT,ENTITY_NOT_FOUND,String.valueOf(accountDto.getId()));
    }


    private RuntimeException exception (EntityType entityType, ExceptionType exceptionType, String... args){
        return WalletException.throwException(entityType,exceptionType,args);
    }
}
