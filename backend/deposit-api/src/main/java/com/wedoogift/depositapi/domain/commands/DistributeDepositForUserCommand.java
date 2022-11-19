package com.wedoogift.depositapi.domain.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;

public record DistributeDepositForUserCommand(AbstractDeposit deposit, @TargetAggregateIdentifier String userId) {
}
