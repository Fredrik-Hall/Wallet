package com.casino.wallet.exception;


import lombok.Getter;

@Getter
public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    INTERNAL_EXCEPTION("internal"),
    NOT_ENOUGH_FUNDS("not.enough.funds"),
    NOT_ALLOWED("not.allowed");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

}
