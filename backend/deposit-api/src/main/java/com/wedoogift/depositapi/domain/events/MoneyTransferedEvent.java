package com.wedoogift.depositapi.domain.events;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.Amount;

public record MoneyTransferedEvent(@NotNull @Valid Amount amount, @NotNull @TargetAggregateIdentifier String companyId) {
}
