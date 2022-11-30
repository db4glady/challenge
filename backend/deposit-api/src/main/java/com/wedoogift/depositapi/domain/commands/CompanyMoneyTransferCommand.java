package com.wedoogift.depositapi.domain.commands;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.Amount;

public record CompanyMoneyTransferCommand(@NotNull @Valid Amount amount, @NotNull @TargetAggregateIdentifier String companyId) {
}
