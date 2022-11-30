package com.wedoogift.depositapi.controllers.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.wedoogift.depositapi.domain.entities.Amount;

public record CreateMoneyTransfer(
        @NotNull(message = "Company should not be null") String companyId,
        @NotNull(message = "Amount should not be null") @Valid Amount amount) {
}
