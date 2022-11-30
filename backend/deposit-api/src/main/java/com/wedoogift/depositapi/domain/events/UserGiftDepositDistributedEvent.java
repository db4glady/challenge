package com.wedoogift.depositapi.domain.events;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.GiftDeposit;
import com.wedoogift.depositapi.domain.entities.MealDeposit;

public record UserGiftDepositDistributedEvent(@NotNull @Valid GiftDeposit deposit, @NotNull @TargetAggregateIdentifier String userId) {
}
