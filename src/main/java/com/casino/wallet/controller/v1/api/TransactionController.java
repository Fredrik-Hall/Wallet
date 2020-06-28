package com.casino.wallet.controller.v1.api;

import com.casino.wallet.dto.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/transaction")
public class TransactionController {


    @Autowired
    public TransactionController(){
    }

    @GetMapping("/history")
    public Response GetTransactions() {

        return Response.ok();
    }

    @GetMapping("/history/{playerId}")
    public Response GetTransactions(@PathVariable long playerId) {

        return Response.ok();
    }
    @GetMapping("/history/{transactionId}")
    public Response GetTransaction(@PathVariable long transactionId ) {

        return Response.ok();
    }

    @GetMapping("/history/round/{roundId}")
    public Response GetTransactionsByRound(@PathVariable long roundId ) {

        return Response.ok();
    }


    @PostMapping("/withdraw/{playerId}")
    public Response Withdraw(@PathVariable long playerId) {

        return Response.ok();
    }

    @PostMapping("/deposit/{playerId}")
    public Response Deposit(@PathVariable long playerId) {

        return Response.ok();
    }

    @PutMapping("/cancel/{playerId}")
    public Response Cancel (@PathVariable long playerId){

        return Response.ok();
    }
}
