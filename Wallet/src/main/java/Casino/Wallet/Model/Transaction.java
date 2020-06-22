package Casino.Wallet.Model;

public class Transaction {

    private final double amount;
    private final int playerId;
    private final String transactionId;

    public Transaction(double amount, int playerId, String transactionId) {

        this.amount = amount;
        this.playerId = playerId;
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
