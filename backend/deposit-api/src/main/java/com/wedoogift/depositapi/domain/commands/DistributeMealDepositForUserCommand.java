package com.wedoogift.depositapi.domain.commands;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.MealDeposit;

public record DistributeMealDepositForUserCommand(@NotNull @Valid MealDeposit deposit, @NotNull @TargetAggregateIdentifier String userId) {
}