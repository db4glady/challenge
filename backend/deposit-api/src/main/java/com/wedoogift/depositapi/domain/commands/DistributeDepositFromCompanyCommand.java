package com.wedoogift.depositapi.domain.commands;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;

public record DistributeDepositFromCompanyCommand(@NotNull @Valid AbstractDeposit deposit, @NotNull @TargetAggregateIdentifier String companyId) {
}
