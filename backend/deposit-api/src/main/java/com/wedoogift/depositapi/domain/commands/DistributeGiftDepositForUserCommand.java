package com.wedoogift.depositapi.domain.commands;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.GiftDeposit;

public record DistributeGiftDepositForUserCommand(@NotNull @Valid GiftDeposit deposit, @NotNull @TargetAggregateIdentifier String userId) {
}
