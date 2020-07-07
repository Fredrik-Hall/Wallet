package com.casino.wallet.controller.v1.api;

import com.casino.wallet.controller.v1.request.CancelRequest;
import com.casino.wallet.controller.v1.request.DepositRequest;
import com.casino.wallet.controller.v1.request.WithdrawRequest;
import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.dto.response.Response;
import com.casino.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {


    @Autowired
    private TransactionService transactionService;

    @GetMapping("/history")
    public Response GetTransactions() {

        List<TransactionDto> transactionDtos = transactionService.getAllTransactions();
        if (!transactionDtos.isEmpty()){
            return Response.ok().setPayload(transactionDtos);
        }
        return Response.notFound().setErrors("No transactions found");
    }

    @GetMapping("/history/{playerId}")
    public Response GetTransactions(@PathVariable long playerId) {
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsByPlayerId(playerId);
        if (!transactionDtos.isEmpty()){
            return Response.ok().setPayload(transactionDtos);
        }
        return Response.notFound().setErrors("No transactions found");
    }

    @GetMapping("/history/{transactionId}")
    public Response GetTransaction(@PathVariable long transactionId ) {
        return Response.ok().setPayload(transactionService.getTransaction(transactionId));
    }

    @GetMapping("/history/round/{roundId}")
    public Response GetTransactionsByRound(@PathVariable long roundId ) {
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsByRoundId(roundId);
        if (!transactionDtos.isEmpty()){
            return Response.ok().setPayload(transactionDtos);
        }
        return Response.notFound().setErrors("No transactions found");
    }


    @PostMapping("/withdraw/{playerId}")
    public Response Withdraw(@PathVariable long playerId, @RequestBody @Valid WithdrawRequest withdrawRequest) {

        return Response.ok();
    }

    @PostMapping("/deposit/{playerId}")
    public Response Deposit(@PathVariable long playerId, @RequestBody @Valid DepositRequest depositRequest) {

        return Response.ok();
    }

    @PutMapping("/cancel/{playerId}")
    public Response Cancel (@PathVariable long playerId, @RequestBody @Valid CancelRequest cancelRequest){

        return Response.ok();
    }
}
