package com.casino.wallet.controller.v1.api;

import com.casino.wallet.controller.v1.request.CancelRequest;
import com.casino.wallet.controller.v1.request.DepositRequest;
import com.casino.wallet.controller.v1.request.WithdrawRequest;
import com.casino.wallet.dto.model.TransactionDto;
import com.casino.wallet.dto.response.Response;
import com.casino.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;

@SuppressWarnings("rawtypes")
@RestController
@Validated
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
        return Response.notFound().setErrors("No transactions found.");
    }

    @GetMapping("/history/player/{playerId}")
    public Response GetTransactions(@PathVariable Long playerId) {
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsByPlayerId(playerId);
        if (!transactionDtos.isEmpty()){
            return Response.ok().setPayload(transactionDtos);
        }
        return Response.notFound().setErrors("No transactions found.");
    }

    @GetMapping("/history/transaction/{transactionId}")
    public Response GetTransaction(@PathVariable Long transactionId ) {
        return Response.ok().setPayload(transactionService.getTransaction(transactionId));
    }

    @GetMapping("/history/round/{roundId}")
    public Response GetTransactionsByRound(@PathVariable Long roundId ) {
        List<TransactionDto> transactionDtos = transactionService.getAllTransactionsByRoundId(roundId);
        if (!transactionDtos.isEmpty()){
            return Response.ok().setPayload(transactionDtos);
        }
        return Response.notFound().setErrors("No transactions found.");
    }


    @PostMapping("/withdraw/{playerId}")
    public Response Withdraw(@PathVariable Long playerId,
                             @RequestBody @Valid WithdrawRequest withdrawRequest) {
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(withdrawRequest.getTransactionId())
                .setAmount(withdrawRequest.getAmount())
                .setRoundId(withdrawRequest.getRoundId())
                .setPlayerId(playerId);

        return Response.ok().setPayload(transactionService.withdraw(transactionDto));
    }

    @PostMapping("/deposit/{playerId}")
    public Response Deposit(@PathVariable Long playerId,
                            @RequestBody @Valid DepositRequest depositRequest) {
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(depositRequest.getTransactionId())
                .setAmount(depositRequest.getAmount())
                .setRoundId(depositRequest.getRoundId())
                .setPlayerId(playerId);

        return Response.ok().setPayload(transactionService.deposit(transactionDto));
    }

    @PutMapping("/cancel/{playerId}")
    public Response Cancel(@PathVariable Long playerId,
                            @RequestBody @Valid CancelRequest cancelRequest){
        TransactionDto transactionDto = new TransactionDto()
                .setTransactionId(cancelRequest.getTransactionId())
                .setAmount(cancelRequest.getAmount())
                .setRoundId(cancelRequest.getRoundId())
                .setPlayerId(playerId);

        return Response.ok().setPayload(transactionService.cancel(transactionDto));
    }
}
