package com.casino.wallet.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Valid
public class UpdateAccountRequest {

    @PositiveOrZero(message = "Amount must be positive or zero")
    private double amount;
}
