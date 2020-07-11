package com.casino.wallet.controller.v1;

import com.casino.wallet.controller.v1.api.AccountController;
import com.casino.wallet.controller.v1.request.UpdateAccountRequest;
import com.casino.wallet.dto.model.AccountDto;

import com.casino.wallet.service.AccountService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    private final HttpHeaders authHeader = createHeaders();

    //GetAccount
    @Test
    public void GetAccount_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        AccountDto accountDto = new AccountDto().setId(playerId).setAmount(10.0);
        Mockito.when(accountService.getAccountByPlayerId(playerId))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(get("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is(1)))
                .andExpect(jsonPath("$.payload.amount", is(10.0)));
    }

    @Test
    public void GetAccount_whenCalledWithoutAuthenticationHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        AccountDto accountDto = new AccountDto().setId(playerId).setAmount(10.0);
        Mockito.when(accountService.getAccountByPlayerId(playerId))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(get("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //UpdateAccount
    @Test
    public void UpdateAccount_whenCalledCorrectly_thenShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        double amount = 10.0;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest().setAmount(amount);
        AccountDto accountDto = new AccountDto()
                .setAmount(amount)
                .setId(playerId);
        Mockito.when(accountService.updateAccount(any(AccountDto.class)))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(put("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updateAccountRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id",is(1)))
                .andExpect(jsonPath("$.payload.amount", is(10.0)));
    }

    @Test
    public void UpdateAccount_whenCalledWithNegativeAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        double amount = -1;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest().setAmount(amount);
        AccountDto accountDto = new AccountDto()
                .setAmount(amount)
                .setId(playerId);
        Mockito.when(accountService.updateAccount(any(AccountDto.class)))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(put("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updateAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message",is("[Amount must be positive or zero.]")));
    }

    @Test
    public void UpdateAccount_whenCalledWithNullAmount_thenShouldReturnBadRequest() throws Exception {
        //Given
        long playerId = 1;
        double amount = 10.0;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest().setAmount(null);
        AccountDto accountDto = new AccountDto()
                .setAmount(amount)
                .setId(playerId);
        Mockito.when(accountService.updateAccount(any(AccountDto.class)))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(put("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updateAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status",is("VALIDATION_EXCEPTION")))
                .andExpect(jsonPath("$.errors.message",is("[Amount cannot be null.]")));
    }

    @Test
    public void UpdateAccount_whenCalledWithoutAuthHeader_thenShouldReturnUnauthorized() throws Exception {
        //Given
        long playerId = 1;
        double amount = 10.0;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest().setAmount(null);
        AccountDto accountDto = new AccountDto()
                .setAmount(amount)
                .setId(playerId);
        Mockito.when(accountService.updateAccount(any(AccountDto.class)))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(put("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(updateAccountRequest)))
                .andExpect(status().isUnauthorized());
    }


    HttpHeaders createHeaders(){
        return new HttpHeaders() {{
            String auth = "walletuser" + ":" + "password123";
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
