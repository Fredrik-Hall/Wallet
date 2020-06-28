package com.casino.wallet.service;

import com.casino.wallet.dto.model.AccountDto;

public interface AccountService {

    AccountDto getAccountByPlayerId(Long playerId);

    AccountDto updateAccount(AccountDto accountDto);
}
