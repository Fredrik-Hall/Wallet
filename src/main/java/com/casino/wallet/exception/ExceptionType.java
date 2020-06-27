package com.casino.wallet.exception;


import lombok.Getter;

@Getter
public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    ENTITY_EXCEPTION("exception"),
    DELETION_EXCEPTION("deletion");

    String value;

    ExceptionType(String value){this.value = value; }

}
