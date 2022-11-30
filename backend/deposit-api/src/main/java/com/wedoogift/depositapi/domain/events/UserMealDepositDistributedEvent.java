package com.wedoogift.depositapi.domain.events;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;
import com.wedoogift.depositapi.domain.entities.MealDeposit;

public record UserMealDepositDistributedEvent(@NotNull @Valid MealDeposit deposit, @NotNull @TargetAggregateIdentifier String userId) {
}
