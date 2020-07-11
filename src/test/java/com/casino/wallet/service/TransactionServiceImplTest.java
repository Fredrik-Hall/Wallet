package com.casino.wallet.service;

import com.casino.wallet.config.ExceptionConfig;
import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.exception.WalletException;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.model.player.Player;
import com.casino.wallet.model.transaction.Transaction;
import com.casino.wallet.repository.AccountRepository;
import com.casino.wallet.repository.PlayerRepository;
import com.casino.wallet.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private ExceptionConfig exceptionConfig;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    private final Player playerOne = new Player().setFirstName("Bruce").setLastName("Wayne").setEmail("Bruce@wayneenterprises.com").setId(1).setCreated(Instant.now());
    private final Player playerTwo = new Player().setFirstName("Clark").setLastName("Kent").setEmail("Clarke.Kent@dailyplanet.com").setId(2).setCreated(Instant.now());
    private final Player playerThree = new Player().setFirstName("Diana").setLastName("Prince").setEmail("Diana1245@gmail.com").setId(3).setCreated(Instant.now());
    private final Player playerFour = new Player().setFirstName("Barry").setLastName("Allen").setEmail("Barry.Allen@ccpd.gov").setId(4).setCreated(Instant.now());
    private final Account playerOneAccount = new Account().setId(1).setAmount(100);
    private final Account playerTwoAccount = new Account().setId(2).setAmount(10);

    private final Transaction bet = new Transaction()
            .setTransactionId(1)
            .setAmount(1)
            .setPlayerId(1)
            .setRoundId(1)
            .setWithdrawal(true)
            .setCancelled(false)
            .setId(1)
            .setCreated(Instant.now());
    private final Transaction win = new Transaction()
            .setTransactionId(2)
            .setAmount(1)
            .setPlayerId(1)
            .setRoundId(1)
            .setWithdrawal(false)
            .setCancelled(false)
            .setId(2)
            .setCreated(Instant.now());
    private final Transaction cancelledBet = new Transaction()
            .setTransactionId(3)
            .setAmount(1)
            .setPlayerId(2)
            .setRoundId(2)
            .setWithdrawal(true)
            .setCancelled(true)
            .setId(3)
            .setCreated(Instant.now());
    private final Transaction win2 = new Transaction()
            .setTransactionId(4)
            .setAmount(1)
            .setPlayerId(2)
            .setRoundId(2)
            .setWithdrawal(false)
            .setCancelled(false)
            .setId(4)
            .setCreated(Instant.now());
    private final Transaction nonExisting = new Transaction()
            .setTransactionId(5)
            .setAmount(1)
            .setPlayerId(2)
            .setRoundId(3)
            .setWithdrawal(true)
            .setCancelled(false)
            .setId(5)
            .setCreated(Instant.now());
    private final Transaction bet2 = new Transaction()
            .setTransactionId(6)
            .setAmount(1)
            .setPlayerId(1)
            .setRoundId(4)
            .setWithdrawal(true)
            .setCancelled(false)
            .setId(6)
            .setCreated(Instant.now());
    private final Transaction win3 = new Transaction()
            .setTransactionId(7)
            .setAmount(1)
            .setPlayerId(1)
            .setRoundId(4)
            .setWithdrawal(false)
            .setCancelled(false)
            .setId(7)
            .setCreated(Instant.now());

    private final List<Transaction> transactionList = Arrays.asList(bet,win,cancelledBet,win2);
    private final List<Transaction> transactionListRoundOne = Arrays.asList(bet,win);
    private final List<Transaction> transactionListPlayerTwo = Arrays.asList(cancelledBet,win2);

    @Before
    public void setUp(){

        //Empty Responses
        Optional<Account> emptyAccount = Optional.empty();
        Optional<Player> emptyPlayer = Optional.empty();
        Optional<Transaction> emptyTransaction = Optional.empty();

        //Transaction Repository Mocks
        Mockito.when(transactionRepository.findAll()).thenReturn(transactionList);
        Mockito.when(transactionRepository.findByPlayerId(cancelledBet.getPlayerId()))
                .thenReturn(transactionListPlayerTwo);

        Mockito.when(transactionRepository.findByTransactionId(nonExisting.getId()))
                .thenReturn(emptyTransaction);
        Mockito.when(transactionRepository.findByTransactionId(bet.getId()))
                .thenReturn(Optional.of(bet));
        Mockito.when(transactionRepository.findByTransactionId(cancelledBet.getId()))
                .thenReturn(Optional.of(cancelledBet));
        Mockito.when(transactionRepository.findByTransactionId(win3.getId()))
                .thenReturn(Optional.of(win3));

        Mockito.when(transactionRepository.findByRoundId(win.getRoundId()))
                .thenReturn(transactionListRoundOne);

        //Player Repository Mocks
        Mockito.when(playerRepository.findById(playerOne.getId()))
                .thenReturn(Optional.of(playerOne));
        Mockito.when(playerRepository.findById(playerTwo.getId()))
                .thenReturn(Optional.of(playerTwo));
        Mockito.when(playerRepository.findById(playerThree.getId()))
                .thenReturn(emptyPlayer);
        Mockito.when(playerRepository.findById(playerFour.getId()))
                .thenReturn(Optional.of(playerFour));

        //Account Repository Mocks
        Mockito.when(accountRepository.findById(playerOne.getId()))
                .thenReturn(Optional.of(playerOneAccount));
        Mockito.when(accountRepository.findById(playerTwo.getId()))
                .thenReturn(Optional.of(playerTwoAccount));
        Mockito.when(accountRepository.findById(playerFour.getId()))
                .thenReturn(emptyAccount);

        // Exception Config Mocks

        String transactionNotAllowedException = "transaction.not.allowed";
        Mockito.when(exceptionConfig.getConfigValue(transactionNotAllowedException))
                .thenReturn("{0} is not allowed.");

        String transactionNotEnoughFundsException = "transaction.not.enough.funds";
        Mockito.when(exceptionConfig.getConfigValue(transactionNotEnoughFundsException))
                .thenReturn("Player {0} does not have enough funds. Request amount {1} is larger than Account amount {2}.");

        String transactionDuplicateException = "transaction.duplicate";
        Mockito.when(exceptionConfig.getConfigValue(transactionDuplicateException))
                .thenReturn("Transaction with Id {0} already exists for another player.");

        String playerNotFoundException = "player.not.found";
        Mockito.when(exceptionConfig.getConfigValue(playerNotFoundException))
                .thenReturn("Player with id {0} was not found.");

        String transactionNotFoundException = "transaction.not.found";
        Mockito.when(exceptionConfig.getConfigValue(transactionNotFoundException))
                .thenReturn("Transaction with id {0} was not found.");

        String accountNotFoundException = "account.not.found";
        Mockito.when(exceptionConfig.getConfigValue(accountNotFoundException))
                .thenReturn("Account for player with id {0} was not found.");

    }

    @Test
    public void getAllTransactions_whenCalled_thenAListOfTransactionsShouldBeReturned(){
        //Given
        long expectedTransactionId = bet.getId();
        double expectedAmount = bet.getAmount();
        long expectedPlayerId = bet.getPlayerId();
        long expectedRoundId = bet.getRoundId();
        int expectedTransactionCount = transactionList.size();

        //When
        List<TransactionDto> transactionDtos = transactionService.getAllTransactions();
        TransactionDto transactionOne = transactionDtos.get(0);

        //Then
        assertThat(transactionDtos.size())
                .isEqualTo(expectedTransactionCount);
        assertThat(transactionOne.getId())
                .isEqualTo(expectedTransactionId);
        assertThat(transactionOne.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transactionOne.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transactionOne.getRoundId())
                .isEqualTo(expectedRoundId);

    }

    @Test
    public void getAllTransactionsByPlayerId_whenCalled_thenAListOfTransactionsShouldBeReturned(){
        //Given
        long expectedTransactionId = cancelledBet.getId();
        double expectedAmount = cancelledBet.getAmount();
        long expectedPlayerId = playerTwo.getId();
        long expectedRoundId = cancelledBet.getRoundId();
        int expectedTransactionCount = transactionListPlayerTwo.size();

        //When
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsByPlayerId(expectedPlayerId);
        TransactionDto transactionOne = transactionDtos.get(0);

        //Then
        assertThat(transactionDtos.size())
                .isEqualTo(expectedTransactionCount);
        assertThat(transactionOne.getId())
                .isEqualTo(expectedTransactionId);
        assertThat(transactionOne.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transactionOne.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transactionOne.getRoundId())
                .isEqualTo(expectedRoundId);

    }

    @Test
    public void getAllTransactionsByPlayerId_whenPlayerDoesntExist_thenShouldThrowException(){
        //Given
        long playerId = playerThree.getId();
        String expectedMessage = "Player with id " + playerId + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.getAllTransactionsByPlayerId(playerId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllTransactionsByRoundId_whenCalled_thenAListOfTransactionsShouldBeReturned(){
        //Given
        long expectedTransactionId = bet.getId();
        double expectedAmount = bet.getAmount();
        long expectedPlayerId = bet.getPlayerId();
        long expectedRoundId = bet.getRoundId();
        int expectedTransactionCount = transactionListRoundOne.size();

        //When
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsByRoundId(expectedRoundId);
        TransactionDto transactionOne = transactionDtos.get(0);

        //Then
        assertThat(transactionDtos.size())
                .isEqualTo(expectedTransactionCount);
        assertThat(transactionOne.getId())
                .isEqualTo(expectedTransactionId);
        assertThat(transactionOne.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transactionOne.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transactionOne.getRoundId())
                .isEqualTo(expectedRoundId);
    }

    @Test
    public void getTransaction_whenCalled_thenShouldReturnSingleTransaction(){
        //Given
        long expectedTransactionId = bet.getId();
        double expectedAmount = bet.getAmount();
        long expectedPlayerId = bet.getPlayerId();
        long expectedRoundId = bet.getRoundId();

        //When
        TransactionDto transaction = transactionService.getTransaction(bet.getId());

        //Then
        assertThat(transaction.getId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
    }
    @Test
    public void getTransaction_whenTransactionDoesntExist_thenShouldThrowException(){
        //Given
        long transactionId = nonExisting.getId();
        String expectedMessage = "Transaction with id "+ transactionId + " was not found.";


        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.getTransaction(transactionId));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void withdraw_whenCalled_thenReturnNewTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(bet2.getId())
                .setPlayerId(bet2.getPlayerId())
                .setRoundId(bet2.getRoundId())
                .setAmount(bet2.getAmount());
        Long expectedTransactionId = bet2.getTransactionId();
        Long expectedPlayerId = bet2.getPlayerId();
        Long expectedRoundId = bet2.getRoundId();
        double expectedAmount = bet2.getAmount();
        double expectedAccountAmount = playerOneAccount.getAmount();

        Mockito.when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(bet2);
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(playerOneAccount);

        //When
        TransactionDto transaction = transactionService.withdraw(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);

    }

    @Test
    public void withdraw_whenExactTransactionExists_thenReturnThatTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(bet.getId())
                .setPlayerId(bet.getPlayerId())
                .setRoundId(bet.getRoundId())
                .setAmount(bet.getAmount());
        Long expectedTransactionId = bet.getTransactionId();
        Long expectedPlayerId = bet.getPlayerId();
        Long expectedRoundId = bet.getRoundId();
        double expectedAmount = bet.getAmount();
        double expectedAccountAmount = playerOneAccount.getAmount();

        //When
        TransactionDto transaction = transactionService.withdraw(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);

    }

    @Test
    public void withdraw_whenNonExistentPlayer_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setPlayerId(playerThree.getId());
        String expectedMessage = "Player with id " + transactionDto.getPlayerId() + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.withdraw(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void withdraw_whenNonExistentAccount_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setPlayerId(playerFour.getId());
        String expectedMessage = "Account for player with id " + transactionDto.getPlayerId() + " was not found.";


        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.withdraw(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void withdraw_whenTransactionExistsForOtherPlayer_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(bet2.getId())
                .setPlayerId(bet2.getPlayerId())
                .setRoundId(bet2.getRoundId())
                .setAmount(bet2.getAmount());
        String expectedMessage = "Transaction with Id " + transactionDto.getTransactionId() + " already exists for another player.";
        Mockito.when(transactionRepository.findByTransactionId(transactionDto.getTransactionId()))
                .thenReturn(Optional.ofNullable(bet));

        //When
        Exception exception = assertThrows(WalletException.DuplicateEntityException.class, () -> transactionService.withdraw(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void withdraw_whenNotEnoughMoney_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(bet2.getId())
                .setPlayerId(bet2.getPlayerId())
                .setRoundId(bet2.getRoundId())
                .setAmount(playerOneAccount.getAmount()+1);
        String expectedMessage = "Player " + transactionDto.getPlayerId() + " does not have enough funds. Request amount " + transactionDto.getAmount() + " is larger than Account amount " + playerOneAccount.getAmount() + ".";

        //When
        Exception exception = assertThrows(WalletException.NotEnoughFundsException.class, () -> transactionService.withdraw(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deposit_whenCalled_thenReturnNewTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(win.getId())
                .setPlayerId(win.getPlayerId())
                .setRoundId(win.getRoundId())
                .setAmount(win.getAmount());
        Long expectedTransactionId = win.getTransactionId();
        Long expectedPlayerId = win.getPlayerId();
        Long expectedRoundId = win.getRoundId();
        double expectedAmount = win.getAmount();
        double expectedAccountAmount = playerOneAccount.getAmount();

        Mockito.when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(win);
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(playerOneAccount);

        //When
        TransactionDto transaction = transactionService.deposit(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);

    }

    @Test
    public void deposit_whenExactTransactionExists_thenReturnThatTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(win3.getId())
                .setPlayerId(win3.getPlayerId())
                .setRoundId(win3.getRoundId())
                .setAmount(win3.getAmount());
        Long expectedTransactionId = win3.getTransactionId();
        Long expectedPlayerId = win3.getPlayerId();
        Long expectedRoundId = win3.getRoundId();
        double expectedAmount = win3.getAmount();
        double expectedAccountAmount = playerOneAccount.getAmount();

        //When
        TransactionDto transaction = transactionService.withdraw(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);

    }
    @Test
    public void deposit_whenNonExistentPlayer_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setPlayerId(playerThree.getId());
        String expectedMessage = "Player with id " + transactionDto.getPlayerId() + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.deposit(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deposit_whenNonExistentAccount_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setPlayerId(playerFour.getId());
        String expectedMessage = "Account for player with id " + transactionDto.getPlayerId() + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.deposit(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deposit_whenTransactionExistsForOtherPlayer_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(win2.getId())
                .setPlayerId(win2.getPlayerId())
                .setRoundId(win2.getRoundId())
                .setAmount(win2.getAmount());
        String expectedMessage = "Transaction with Id " + transactionDto.getTransactionId() + " already exists for another player.";
        Mockito.when(transactionRepository.findByTransactionId(transactionDto.getTransactionId()))
                .thenReturn(Optional.ofNullable(win));

        //When
        Exception exception = assertThrows(WalletException.DuplicateEntityException.class, () -> transactionService.deposit(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void cancel_whenCancellingARegularBet_thenShouldReturnTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(bet.getId())
                .setPlayerId(bet.getPlayerId())
                .setRoundId(bet.getRoundId())
                .setAmount(bet.getAmount());
        Long expectedTransactionId = bet.getTransactionId();
        Long expectedPlayerId = bet.getPlayerId();
        Long expectedRoundId = bet.getRoundId();
        double expectedAmount = bet.getAmount();
        double expectedAccountAmount = playerOneAccount.getAmount();
        Mockito.when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(bet);
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(playerOneAccount);

        //When
        TransactionDto transaction = transactionService.cancel(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);
    }

    @Test
    public void cancel_whenCancellingAnAlreadyCancelledBet_thenShouldReturnTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(cancelledBet.getId())
                .setPlayerId(cancelledBet.getPlayerId())
                .setRoundId(cancelledBet.getRoundId())
                .setAmount(cancelledBet.getAmount());
        Long expectedTransactionId = cancelledBet.getTransactionId();
        Long expectedPlayerId = cancelledBet.getPlayerId();
        Long expectedRoundId = cancelledBet.getRoundId();
        double expectedAmount = cancelledBet.getAmount();
        double expectedAccountAmount = playerTwoAccount.getAmount();
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(playerTwoAccount);

        //When
        TransactionDto transaction = transactionService.cancel(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);
    }

    @Test
    public void cancel_whenCancellingANonExistingBet_thenShouldReturnTransaction(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(nonExisting.getId())
                .setPlayerId(nonExisting.getPlayerId())
                .setRoundId(nonExisting.getRoundId())
                .setAmount(nonExisting.getAmount());
        Long expectedTransactionId = nonExisting.getTransactionId();
        Long expectedPlayerId = nonExisting.getPlayerId();
        Long expectedRoundId = nonExisting.getRoundId();
        double expectedAmount = nonExisting.getAmount();
        double expectedAccountAmount = playerTwoAccount.getAmount();

        Mockito.when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(nonExisting);
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(playerTwoAccount);

        //When
        TransactionDto transaction = transactionService.cancel(transactionDto);

        //Then
        assertThat(transaction.getPlayerId())
                .isEqualTo(expectedPlayerId);
        assertThat(transaction.getTransactionId())
                .isEqualTo(expectedTransactionId);
        assertThat(transaction.getRoundId())
                .isEqualTo(expectedRoundId);
        assertThat(transaction.getAmount())
                .isEqualTo(expectedAmount);
        assertThat(transaction.getAccount().getAmount())
                .isEqualTo(expectedAccountAmount);
    }

    @Test
    public void cancel_whenNonExistentPlayer_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setPlayerId(playerThree.getId());
        String expectedMessage = "Player with id " + transactionDto.getPlayerId() + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.cancel(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void cancel_whenNonExistentAccount_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setPlayerId(playerFour.getId());
        String expectedMessage = "Account for player with id " + transactionDto.getPlayerId() + " was not found.";

        //When
        Exception exception = assertThrows(WalletException.EntityNotFoundException.class, () -> transactionService.cancel(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void cancel_whenCancellingADeposit_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(win3.getId())
                .setPlayerId(win3.getPlayerId())
                .setRoundId(win3.getRoundId())
                .setAmount(win3.getAmount());
        String expectedMessage = "Cancelling a deposit transaction is not allowed.";

        //When
        Exception exception = assertThrows(WalletException.NotAllowedException.class, () -> transactionService.cancel(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void cancel_whenCancellingAnotherTransactionThenTheOneIntended_thenShouldThrowException(){
        //Given
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(bet2.getId())
                .setPlayerId(bet2.getPlayerId())
                .setRoundId(bet2.getRoundId())
                .setAmount(bet2.getAmount());
        Mockito.when(transactionRepository.findByTransactionId(transactionDto.getTransactionId()))
                .thenReturn(Optional.ofNullable(bet));

        String expectedMessage = "Cancelling a transaction with non matching values is not allowed.";

        //When
        Exception exception = assertThrows(WalletException.NotAllowedException.class, () -> transactionService.cancel(transactionDto));
        String actualMessage = exception.getMessage();

        //Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
