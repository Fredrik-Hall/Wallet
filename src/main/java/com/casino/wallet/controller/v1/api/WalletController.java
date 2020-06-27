package com.casino.wallet.controller.v1.api;

import com.casino.wallet.model.Transaction.Transaction;
import com.casino.wallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("v1/Wallet")
public class WalletController{

    private final TransactionRepository transactionRepository;

    @Autowired
    public WalletController(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/Balance/{PlayerId}")
    public void Balance(@PathVariable int PlayerId) {

    }

    @GetMapping("/History/{PlayerId}")
    public void History(@PathVariable int PlayerId) {

    }

    @PostMapping("/Withdraw")
    public void Withdraw(Transaction transaction) {
    }

    @PostMapping("/Deposit")
    public void Deposit(Transaction transaction) {
    }
}
