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

    private final String authUsername = "walletuser";
    private final String authPassword = "password123";
    private final HttpHeaders authHeader = createHeaders(authUsername,authPassword);

    @Test
    public void GetAccount_ShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        AccountDto accountDto = new AccountDto().setId(1).setAmount(10.0);
        Mockito.when(accountService.getAccountByPlayerId(playerId))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(get("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id", is((int)accountDto.getId())))
                .andExpect(jsonPath("$.payload.amount", is(accountDto.getAmount())));
    }

    @Test
    public void UpdateAccount_ShouldReturnOK() throws Exception {
        //Given
        long playerId = 1;
        double amount = 10.0;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest().setAmount(amount);
        AccountDto accountDto = new AccountDto()
                .setAmount(amount)
                .setId(playerId);
        Mockito.when(accountService.updateAccount(accountDto))
                .thenReturn(accountDto);

        //When && Then
        mvc.perform(put("/v1/account/"+playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(authHeader)
                .content(new ObjectMapper().writeValueAsBytes(updateAccountRequest)))
                .andExpect(status().isOk());
    }


    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
