package com.wedoogift.depositapi.domain.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;

public record DistributeDepositFromCompanyCommand(AbstractDeposit deposit, @TargetAggregateIdentifier String companyId) {
}
