package com.casino.wallet.dto.model;

import com.casino.wallet.model.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

    private long id;

    private long playerId;

    private Instant created;

    private long transactionId;

    private long roundId;

    private boolean withdrawal;

    private double amount;

    private boolean cancelled;

    private Account account;

}
