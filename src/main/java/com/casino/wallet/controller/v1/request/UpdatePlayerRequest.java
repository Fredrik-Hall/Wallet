package com.casino.wallet.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Valid
public class UpdatePlayerRequest {
    @NotEmpty(message = "First name cannot be empty.")
    private String firstName;
    @NotEmpty(message = "Last name cannot be empty.")
    private String lastName;
    @Email(message = "Email must be valid.")
    @NotEmpty(message = "Email cannot be empty.")
    private String email;
}
