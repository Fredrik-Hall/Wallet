package com.casino.wallet.service;

import com.casino.wallet.config.ExceptionConfig;
import com.casino.wallet.dto.model.AccountDto;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private ExceptionConfig exceptionConfig;

    @MockBean
    private AccountRepository accountRepository;

    private final Account accountOne = new Account().setId(1).setAmount(100.0);
    private final Account secondAccount = new Account().setId(2).setAmount(100.0);
    private final Account thirdAccount = new Account().setId(3).setAmount(0.0);
    private final Optional<Account> emptyAccount = Optional.empty();


    @Before
    public void setUp() {


        String playerNotFoundException = "account.not.found";
        Mockito.when(exceptionConfig.getConfigValue(playerNotFoundException))
                .thenReturn("Player with id {0} was not found.");

        Mockito.when(accountRepository.findById(accountOne.getId()))
                .thenReturn(java.util.Optional.of(accountOne));
        Mockito.when(accountRepository.findById(secondAccount.getId()))
                .thenReturn(java.util.Optional.of(secondAccount));

        Mockito.when(accountRepository.findById(thirdAccount.getId()))
                .thenReturn(emptyAccount);
    }

    @Test
    public void getAccountByPlayerId_whenValidId_thenAccountShouldBeFound(){

        //Given
        long playerId = accountOne.getId();

        //When
        AccountDto found = accountService.getAccountByPlayerId(playerId);


        ////Then
        assertThat(found.getId())
                .isEqualTo(accountOne.getId());
        assertThat(found.getAmount())
                .isEqualTo(accountOne.getAmount());
    }

    @Test
    public void getAccountByPlayerId_whenInvalidId_thenExceptionShouldBeThrown(){

        //Given
        long playerId = thirdAccount.getId();
        String expectedMessage = "Player with id "+ playerId + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> accountService.getAccountByPlayerId(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateAccount_whenValidId_thenAccountShouldBeFound(){

        //Given
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(secondAccount);
        AccountDto accountDto = new AccountDto().setId(secondAccount.getId()).setAmount(secondAccount.getAmount());

        //When
        AccountDto updatedAccount = accountService.updateAccount(accountDto);


        //Then
        assertThat(updatedAccount.getId())
                .isEqualTo(accountDto.getId());
        assertThat(updatedAccount.getAmount())
                .isEqualTo(accountDto.getAmount());
    }

    @Test
    public void updateAccount_whenInvalidId_thenExceptionShouldBeThrown(){

        //Given
        AccountDto accountDto = new AccountDto().setId(thirdAccount.getId());
        String expectedMessage = "Player with id "+ accountDto.getId() +" was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> accountService.updateAccount(accountDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
