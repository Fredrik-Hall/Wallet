package com.casino.wallet.controller.v1;

import com.casino.wallet.model.wallet.Balance;
import com.casino.wallet.model.wallet.Transaction;
import com.casino.wallet.model.wallet.TransactionResponse;
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
    public Balance Balance(@PathVariable int PlayerId) {
        return null;
    }

    @GetMapping("/History/{PlayerId}")
    public ArrayList<Transaction> History(@PathVariable int PlayerId) {
        return null;
    }

    @PostMapping("/Withdraw")
    public TransactionResponse Withdraw(Transaction transaction) {
        transactionRepository.save(transaction);
        return null;
    }

    @PostMapping("/Deposit")
    public TransactionResponse Deposit(Transaction transaction) {
        System.out.println(transactionRepository.findByplayerId(transaction.getPlayerId()).toString());
        return null;
    }
}
