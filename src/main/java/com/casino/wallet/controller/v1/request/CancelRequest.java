package com.casino.wallet.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Valid
public class CancelRequest {

    @NotNull(message = "TransactionId cannot be null.")
    private Long transactionId;

    @NotNull(message = "RoundId cannot be null.")
    private Long roundId;

    @Positive(message = "The amount must be positive.")
    @NotNull(message = "Amount cannot be null.")
    private Double amount;
}
