package Casino.Wallet;

import Casino.Wallet.Model.BalanceResponse;
import Casino.Wallet.Model.Transaction;
import Casino.Wallet.Model.TransactionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("/default")
public interface IWalletController {

    @GetMapping("/Balance/{PlayerId}")
    BalanceResponse Balance(@PathVariable int PlayerId);

    @GetMapping("/History/{PlayerId}")
    ArrayList<Transaction> History(@PathVariable int PlayerId);

    @PostMapping("/Withdraw")
    TransactionResponse Withdraw(@RequestBody Transaction transaction);

    @PostMapping("/Deposit")
    TransactionResponse Deposit(@RequestBody Transaction transaction);
}
