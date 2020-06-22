package Casino.Wallet;

import Casino.Wallet.Model.BalanceResponse;
import Casino.Wallet.Model.ErrorCode;
import Casino.Wallet.Model.Transaction;
import Casino.Wallet.Model.TransactionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/Wallet")
public class WalletController implements IWalletController{

    @Override
    public BalanceResponse Balance(int PlayerId) {
        return null;
    }

    @Override
    public ArrayList<Transaction> History(int PlayerId) {
        return null;
    }

    @Override
    public TransactionResponse Withdraw(Transaction transaction) {
        return null;
    }

    @Override
    public TransactionResponse Deposit(Transaction transaction) {
        return null;
    }
}
