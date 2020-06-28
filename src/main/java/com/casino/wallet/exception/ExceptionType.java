package com.casino.wallet.exception;


import lombok.Getter;

@Getter
public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    ENTITY_EXCEPTION("exception"),
    INTERNAL_EXCEPTION("internal");

    String value;

    ExceptionType(String value){this.value = value; }

}
