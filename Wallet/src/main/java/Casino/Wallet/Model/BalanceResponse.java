package Casino.Wallet.Model;

public class BalanceResponse {

    private ErrorCode errorCode;
    private Double amount;

    public BalanceResponse(ErrorCode errorCode, Double amount) {
        this.errorCode = errorCode;
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
