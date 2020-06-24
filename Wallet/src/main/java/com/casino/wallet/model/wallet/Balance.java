package com.casino.wallet.model.wallet;

import com.casino.wallet.model.ErrorCode;

public class Balance {

    private ErrorCode errorCode;
    private Double amount;

    public Balance(ErrorCode errorCode, Double amount) {
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
