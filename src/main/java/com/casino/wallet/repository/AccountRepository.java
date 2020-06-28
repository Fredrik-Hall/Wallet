package com.casino.wallet.repository;

import com.casino.wallet.model.account.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
