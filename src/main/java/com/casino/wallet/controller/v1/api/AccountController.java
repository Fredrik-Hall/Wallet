package com.casino.wallet.controller.v1.api;

import com.casino.wallet.controller.v1.request.UpdateAccountRequest;
import com.casino.wallet.controller.v1.request.UpdatePlayerRequest;
import com.casino.wallet.dto.model.AccountDto;
import com.casino.wallet.dto.response.Response;
import com.casino.wallet.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{playerId}")
    public Response getAccount(@PathVariable long playerId){
        return Response.ok().setPayload(accountService.getAccountByPlayerId(playerId));
    }

    @PutMapping("/{playerId}")
    public Response updateAccount(@PathVariable long playerId, @RequestBody @Valid UpdateAccountRequest updateAccountRequest){
        AccountDto accountDto = new AccountDto()
                .setId(playerId)
                .setAmount(updateAccountRequest.getAmount());

        return Response.ok().setPayload(accountService.updateAccount(accountDto));

    }
}
