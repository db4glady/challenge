package com.wedoogift.depositapi.controllers.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.wedoogift.depositapi.domain.entities.Amount;

public record CreatePayment(
        @NotNull(message = "User should not be null") String userId,
        @NotNull(message = "Amount should not be null") @Valid Amount amount) {
}
