package com.casino.wallet.model;

public enum ErrorCode {
    OK(0),
    NOT_ENOUGH_FUND (1),
    TRANSACTION_ID_TAKEN (2),
    INVALID_AMOUNT (3),
    INVALID_USER (4),
    INVALID_TRANSACTION_ID (5);


    public final int index;

     ErrorCode(int index) {
        this.index = index;
    }

}



