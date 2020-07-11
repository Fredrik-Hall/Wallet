package com.casino.wallet.controller.v1;

import com.casino.wallet.controller.v1.api.TransactionController;
import com.casino.wallet.controller.v1.request.CancelRequest;
import com.casino.wallet.controller.v1.request.DepositRequest;
import com.casino.wallet.controller.v1.request.WithdrawRequest;
import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.model.account.Account;
import com.casino.wallet.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;

    private final HttpHeaders authHeader = createHeaders();

    HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = "walletuser" + ":" + "password123";
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    //GetTransactions
    @Test
    public void GetTransactions_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        TransactionDto transactionDtoOne = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());
        TransactionDto transactionDtoTwo = new TransactionDto()
                .setId(2L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(20.0).setId(1L))
                .setTransactionId(2L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(10.0)
                .setCreated(Instant.now());
        List<TransactionDto> transactionDtoList = Arrays.asList(transactionDtoOne, transactionDtoTwo);

        Mockito.when(transactionService.getAllTransactions())
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.[0].id", is(1)))
                .andExpect(jsonPath("$.payload.[0].playerId", is(1)))
                .andExpect(jsonPath("$.payload.[0].transactionId", is(1)))
                .andExpect(jsonPath("$.payload.[0].roundId", is(1)))
                .andExpect(jsonPath("$.payload.[0].created", is(transactionDtoOne.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[0].cancelled", is(false)))
                .andExpect(jsonPath("$.payload.[0].withdrawal", is(true)))
                .andExpect(jsonPath("$.payload.[0].amount", is(1.0)))
                .andExpect(jsonPath("$.payload.[0].account.id", is(1)))
                .andExpect(jsonPath("$.payload.[0].account.amount", is(10.0)))
                .andExpect(jsonPath("$.payload.[1].id", is(2)))
                .andExpect(jsonPath("$.payload.[1].playerId", is(1)))
                .andExpect(jsonPath("$.payload.[1].transactionId", is(2)))
                .andExpect(jsonPath("$.payload.[1].roundId", is(1)))
                .andExpect(jsonPath("$.payload.[1].created", is(transactionDtoTwo.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[1].cancelled", is(false)))
                .andExpect(jsonPath("$.payload.[1].withdrawal", is(false)))
                .andExpect(jsonPath("$.payload.[1].amount", is(10.0)))
                .andExpect(jsonPath("$.payload.[1].account.id", is(1)))
                .andExpect(jsonPath("$.payload.[1].account.amount", is(20.0)));
    }

    @Test
    public void GetTransactions_whenCalledAndNoTransactionsExist_thenShouldReturnNotFound() throws Exception {
        //Given
        List<TransactionDto> transactionDtoList = Collections.emptyList();

        Mockito.when(transactionService.getAllTransactions())
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.errors", is("No transactions found.")));
    }

    @Test
    public void GetTransactions_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        TransactionDto transactionDtoOne = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());
        TransactionDto transactionDtoTwo = new TransactionDto()
                .setId(2L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(20.0).setId(1L))
                .setTransactionId(2L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(10.0)
                .setCreated(Instant.now());
        List<TransactionDto> transactionDtoList = Arrays.asList(transactionDtoOne, transactionDtoTwo);

        Mockito.when(transactionService.getAllTransactions())
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //GetTransactions (PlayerId)
    @Test
    public void GetTransactions_playerId_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        TransactionDto transactionDtoOne = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());
        TransactionDto transactionDtoTwo = new TransactionDto()
                .setId(2L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(20.0).setId(1L))
                .setTransactionId(2L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(10.0)
                .setCreated(Instant.now());
        List<TransactionDto> transactionDtoList = Arrays.asList(transactionDtoOne, transactionDtoTwo);

        Mockito.when(transactionService.getAllTransactionsByPlayerId(playerId))
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.[0].id", is(1)))
                .andExpect(jsonPath("$.payload.[0].playerId", is(1)))
                .andExpect(jsonPath("$.payload.[0].transactionId", is(1)))
                .andExpect(jsonPath("$.payload.[0].roundId", is(1)))
                .andExpect(jsonPath("$.payload.[0].created", is(transactionDtoOne.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[0].cancelled", is(false)))
                .andExpect(jsonPath("$.payload.[0].withdrawal", is(true)))
                .andExpect(jsonPath("$.payload.[0].amount", is(1.0)))
                .andExpect(jsonPath("$.payload.[0].account.id", is(1)))
                .andExpect(jsonPath("$.payload.[0].account.amount", is(10.0)))
                .andExpect(jsonPath("$.payload.[1].id", is(2)))
                .andExpect(jsonPath("$.payload.[1].playerId", is(1)))
                .andExpect(jsonPath("$.payload.[1].transactionId", is(2)))
                .andExpect(jsonPath("$.payload.[1].roundId", is(1)))
                .andExpect(jsonPath("$.payload.[1].created", is(transactionDtoTwo.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[1].cancelled", is(false)))
                .andExpect(jsonPath("$.payload.[1].withdrawal", is(false)))
                .andExpect(jsonPath("$.payload.[1].amount", is(10.0)))
                .andExpect(jsonPath("$.payload.[1].account.id", is(1)))
                .andExpect(jsonPath("$.payload.[1].account.amount", is(20.0)));
    }

    @Test
    public void GetTransactions_playerId_whenCalledAndNoTransactionsExist_thenShouldReturnNotFound() throws Exception {
        //Given
        long playerId = 1;
        List<TransactionDto> transactionDtoList = Collections.emptyList();

        Mockito.when(transactionService.getAllTransactionsByPlayerId(playerId))
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.errors", is("No transactions found.")));
    }

    @Test
    public void GetTransactions_playerId_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        TransactionDto transactionDtoOne = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());
        TransactionDto transactionDtoTwo = new TransactionDto()
                .setId(2L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(20.0).setId(1L))
                .setTransactionId(2L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(10.0)
                .setCreated(Instant.now());
        List<TransactionDto> transactionDtoList = Arrays.asList(transactionDtoOne, transactionDtoTwo);

        Mockito.when(transactionService.getAllTransactionsByPlayerId(playerId))
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history/player/" + playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //GetTransactions (RoundId)
    @Test
    public void GetTransactions_roundId_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long roundId = 1;
        TransactionDto transactionDtoOne = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());
        TransactionDto transactionDtoTwo = new TransactionDto()
                .setId(2L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(20.0).setId(1L))
                .setTransactionId(2L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(10.0)
                .setCreated(Instant.now());
        List<TransactionDto> transactionDtoList = Arrays.asList(transactionDtoOne, transactionDtoTwo);

        Mockito.when(transactionService.getAllTransactionsByRoundId(roundId))
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history/round/" + roundId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.[0].id", is(1)))
                .andExpect(jsonPath("$.payload.[0].playerId", is(1)))
                .andExpect(jsonPath("$.payload.[0].transactionId", is(1)))
                .andExpect(jsonPath("$.payload.[0].roundId", is(1)))
                .andExpect(jsonPath("$.payload.[0].created", is(transactionDtoOne.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[0].cancelled", is(false)))
                .andExpect(jsonPath("$.payload.[0].withdrawal", is(true)))
                .andExpect(jsonPath("$.payload.[0].amount", is(1.0)))
                .andExpect(jsonPath("$.payload.[0].account.id", is(1)))
                .andExpect(jsonPath("$.payload.[0].account.amount", is(10.0)))
                .andExpect(jsonPath("$.payload.[1].id", is(2)))
                .andExpect(jsonPath("$.payload.[1].playerId", is(1)))
                .andExpect(jsonPath("$.payload.[1].transactionId", is(2)))
                .andExpect(jsonPath("$.payload.[1].roundId", is(1)))
                .andExpect(jsonPath("$.payload.[1].created", is(transactionDtoTwo.getCreated().toString())))
                .andExpect(jsonPath("$.payload.[1].cancelled", is(false)))
                .andExpect(jsonPath("$.payload.[1].withdrawal", is(false)))
                .andExpect(jsonPath("$.payload.[1].amount", is(10.0)))
                .andExpect(jsonPath("$.payload.[1].account.id", is(1)))
                .andExpect(jsonPath("$.payload.[1].account.amount", is(20.0)));
    }

    @Test
    public void GetTransactions_roundId_whenCalledAndNoTransactionsExist_thenShouldReturnNotFound() throws Exception {
        //Given
        long roundId = 1;
        List<TransactionDto> transactionDtoList = Collections.emptyList();

        Mockito.when(transactionService.getAllTransactionsByRoundId(roundId))
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history/round/" + roundId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.errors", is("No transactions found.")));
    }

    @Test
    public void GetTransactions_roundId_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long roundId = 1;
        TransactionDto transactionDtoOne = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());
        TransactionDto transactionDtoTwo = new TransactionDto()
                .setId(2L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(20.0).setId(1L))
                .setTransactionId(2L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(10.0)
                .setCreated(Instant.now());
        List<TransactionDto> transactionDtoList = Arrays.asList(transactionDtoOne, transactionDtoTwo);

        Mockito.when(transactionService.getAllTransactionsByRoundId(roundId))
                .thenReturn(transactionDtoList);

        //When && Then
        mvc.perform(get("/v1/transaction/history/round/" + roundId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //GetTransaction
    @Test
    public void GetTransaction_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long transactionId = 1;
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.getTransaction(transactionId))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(get("/v1/transaction/history/transaction/" + transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.playerId", is(1)))
                .andExpect(jsonPath("$.payload.transactionId", is(1)))
                .andExpect(jsonPath("$.payload.roundId", is(1)))
                .andExpect(jsonPath("$.payload.created", is(transactionDto.getCreated().toString())))
                .andExpect(jsonPath("$.payload.cancelled", is(false)))
                .andExpect(jsonPath("$.payload.withdrawal", is(true)))
                .andExpect(jsonPath("$.payload.amount", is(1.0)))
                .andExpect(jsonPath("$.payload.account.id", is(1)))
                .andExpect(jsonPath("$.payload.account.amount", is(10.0)));
    }

    @Test
    public void GetTransaction_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long transactionId = 1;
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.getTransaction(transactionId))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(get("/v1/transaction/history/transaction/" + transactionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //Withdraw
    @Test
    public void Withdraw_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(10.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.playerId", is(1)))
                .andExpect(jsonPath("$.payload.transactionId", is(1)))
                .andExpect(jsonPath("$.payload.roundId", is(1)))
                .andExpect(jsonPath("$.payload.created", is(transactionDto.getCreated().toString())))
                .andExpect(jsonPath("$.payload.cancelled", is(false)))
                .andExpect(jsonPath("$.payload.withdrawal", is(true)))
                .andExpect(jsonPath("$.payload.amount", is(1.0)))
                .andExpect(jsonPath("$.payload.account.id", is(1)))
                .andExpect(jsonPath("$.payload.account.amount", is(10.0)));
    }

    @Test
    public void Withdraw_whenCalledWithNullTransactionId_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(10.0)
                .setTransactionId(null)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[TransactionId cannot be null.]")));
    }

    @Test
    public void Withdraw_whenCalledWithNullRoundId_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(10.0)
                .setTransactionId(1L)
                .setRoundId(null);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[RoundId cannot be null.]")));
    }

    @Test
    public void Withdraw_whenCalledWithNullAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(null)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Amount cannot be null.]")));
    }

    @Test
    public void Withdraw_whenCalledWithZeroAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(0.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[The amount must be positive.]")));
    }

    @Test
    public void Withdraw_whenCalledWithNegativeAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(-1.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[The amount must be positive.]")));
    }

    @Test
    public void Withdraw_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        WithdrawRequest withdrawRequest = new WithdrawRequest()
                .setAmount(1.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.withdraw(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/withdraw/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(withdrawRequest)))
                .andExpect(status().isUnauthorized());
    }

    //Deposit
    @Test
    public void Deposit_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(10.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.playerId", is(1)))
                .andExpect(jsonPath("$.payload.transactionId", is(1)))
                .andExpect(jsonPath("$.payload.roundId", is(1)))
                .andExpect(jsonPath("$.payload.created", is(transactionDto.getCreated().toString())))
                .andExpect(jsonPath("$.payload.cancelled", is(false)))
                .andExpect(jsonPath("$.payload.withdrawal", is(false)))
                .andExpect(jsonPath("$.payload.amount", is(1.0)))
                .andExpect(jsonPath("$.payload.account.id", is(1)))
                .andExpect(jsonPath("$.payload.account.amount", is(10.0)));
    }

    @Test
    public void Deposit_whenCalledWithNullTransactionId_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(10.0)
                .setTransactionId(null)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[TransactionId cannot be null.]")));
    }

    @Test
    public void Deposit_whenCalledWithNullRoundId_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(10.0)
                .setTransactionId(1L)
                .setRoundId(null);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[RoundId cannot be null.]")));
    }

    @Test
    public void Deposit_whenCalledWithNullAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(null)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Amount cannot be null.]")));
    }

    @Test
    public void Deposit_whenCalledWithZeroAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(0.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[The amount must be positive.]")));
    }

    @Test
    public void Deposit_whenCalledWithNegativeAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(-1.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[The amount must be positive.]")));
    }

    @Test
    public void Deposit_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        DepositRequest depositRequest = new DepositRequest()
                .setAmount(1.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(false)
                .setWithdrawal(false)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(post("/v1/transaction/deposit/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(depositRequest)))
                .andExpect(status().isUnauthorized());
    }

    //Cancel
    @Test
    public void Cancel_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(10.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.cancel(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.playerId", is(1)))
                .andExpect(jsonPath("$.payload.transactionId", is(1)))
                .andExpect(jsonPath("$.payload.roundId", is(1)))
                .andExpect(jsonPath("$.payload.created", is(transactionDto.getCreated().toString())))
                .andExpect(jsonPath("$.payload.cancelled", is(true)))
                .andExpect(jsonPath("$.payload.withdrawal", is(true)))
                .andExpect(jsonPath("$.payload.amount", is(1.0)))
                .andExpect(jsonPath("$.payload.account.id", is(1)))
                .andExpect(jsonPath("$.payload.account.amount", is(10.0)));
    }

    @Test
    public void Cancel_whenCalledWithNullTransactionId_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(10.0)
                .setTransactionId(null)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.cancel(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[TransactionId cannot be null.]")));
    }

    @Test
    public void Cancel_whenCalledWithNullRoundId_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(10.0)
                .setTransactionId(1L)
                .setRoundId(null);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.cancel(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[RoundId cannot be null.]")));
    }

    @Test
    public void Cancel_whenCalledWithNullAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(null)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.cancel(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[Amount cannot be null.]")));
    }

    @Test
    public void Cancel_whenCalledWithZeroAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(0.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.cancel(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[The amount must be positive.]")));
    }

    @Test
    public void Cancel_whenCalledWithNegativeAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(-1.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.cancel(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message", is("[The amount must be positive.]")));
    }

    @Test
    public void Cancel_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        CancelRequest cancelRequest = new CancelRequest()
                .setAmount(1.0)
                .setTransactionId(1L)
                .setRoundId(1L);
        TransactionDto transactionDto = new TransactionDto()
                .setId(1L)
                .setPlayerId(1L)
                .setAccount(new Account().setAmount(10.0).setId(1L))
                .setTransactionId(1L)
                .setRoundId(1L)
                .setCancelled(true)
                .setWithdrawal(true)
                .setAmount(1.0)
                .setCreated(Instant.now());

        Mockito.when(transactionService.deposit(any(TransactionDto.class)))
                .thenReturn(transactionDto);

        //When && Then
        mvc.perform(put("/v1/transaction/cancel/" + playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(cancelRequest)))
                .andExpect(status().isUnauthorized());
    }


}
