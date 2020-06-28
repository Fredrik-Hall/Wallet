package com.casino.wallet.dto.mapper;

import com.casino.wallet.dto.model.AccountDto;
import com.casino.wallet.model.account.Account;

public class AccountMapper {
    public static AccountDto toAccountDto(Account account){
        return new AccountDto()
                .setId(account.getId())
                .setAmount(account.getAmount());
    }
}
