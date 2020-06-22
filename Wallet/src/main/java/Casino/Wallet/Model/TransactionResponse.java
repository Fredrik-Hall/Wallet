package Casino.Wallet.Model;

public class TransactionResponse {

    private ErrorCode errorCode;
    private Double amount;

    public TransactionResponse(ErrorCode errorCode, Double amount) {
        this.errorCode = errorCode;
        this.amount = amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Double getAmount() {
        return amount;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
