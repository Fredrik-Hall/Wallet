package com.casino.wallet.controller.v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/transaction")
public class TransactionController {


    @Autowired
    public TransactionController(){
    }

    @GetMapping("/Balance/{PlayerId}")
    public void Balance(@PathVariable int PlayerId) {

    }

    @GetMapping("/History/{PlayerId}")
    public void History(@PathVariable int playerId) {

    }

    @PostMapping("/Withdraw/{playerId}")
    public void Withdraw() {
    }

    @PostMapping("/Deposit/{playerId}")
    public void Deposit() {
    }
}
