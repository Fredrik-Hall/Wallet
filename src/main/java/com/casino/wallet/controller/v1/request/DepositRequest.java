package com.casino.wallet.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Valid
public class DepositRequest {

    @NotEmpty(message = "PlayerId cannot be empty")
    private long playerId;

    @NotEmpty(message = "TransactionId cannot be empty")
    private long transactionId;

    @NotEmpty(message = "RoundId cannot be empty")
    private long roundId;

    @Positive(message = "The amount must be positive.")
    private double amount;
}
