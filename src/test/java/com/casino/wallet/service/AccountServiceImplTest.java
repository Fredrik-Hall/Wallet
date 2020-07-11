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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class AccountServiceImplTest {

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration{

        @Bean(name="mockAccountService")
        @Primary
        public AccountService accountService(){
            return new AccountServiceImpl();
        }

        @Bean(name="mockWalletException")
        public WalletException walletException(){
            return new WalletException(exceptionConfig());
        }

        @Bean(name="mockExceptionConfig")
        public ExceptionConfig exceptionConfig(){
            return Mockito.mock(ExceptionConfig.class);
        }
    }

    @Autowired
    private AccountService accountService;

    @MockBean
    private ExceptionConfig exceptionConfig;

    @MockBean
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        Account account = new Account().setId(1).setAmount(100.0);
        Account secondAccount = new Account().setId(2).setAmount(100.0);

        Optional<Account> emptyAccount = Optional.empty();

        String playerNotFoundException = "account.not.found";

        Mockito.when(exceptionConfig.getConfigValue(playerNotFoundException))
                .thenReturn("Player with id {0} was not found.");

        Mockito.when(accountRepository.findById(account.getId()))
                .thenReturn(java.util.Optional.of(account));
        Mockito.when(accountRepository.findById(secondAccount.getId()))
                .thenReturn(java.util.Optional.of(secondAccount));

        Mockito.when(accountRepository.findById((long) 3))
                .thenReturn(emptyAccount);

        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(secondAccount);
    }

    @Test
    public void getAccountByPlayerId_whenValidId_thenAccountShouldBeFound(){

        //Given
        long playerId = 1;

        //When
        AccountDto found = accountService.getAccountByPlayerId(playerId);


        ////Then
        assertThat(found.getId())
                .isEqualTo(playerId);
    }

    @Test
    public void getAccountByPlayerId_whenInvalidId_thenExceptionShouldBeThrown(){

        //Given
        long playerId = 3;
        String expectedMessagePart1 = "Player with id";
        String expectedMessagePart2 =  "was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> accountService.getAccountByPlayerId(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));
    }

    @Test
    public void updateAccount_whenValidId_thenAccountShouldBeFound(){

        //Given
        AccountDto accountDto = new AccountDto().setId(2).setAmount(100);

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
        AccountDto accountDto = new AccountDto().setId(3);
        String expectedMessagePart1 = "Player with id";
        String expectedMessagePart2 =  "was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> accountService.updateAccount(accountDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessagePart1));
        assertTrue(actualMessage.contains(expectedMessagePart2));
    }
}
