package com.wedoogift.depositapi.domain.commands;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.MealPayment;

public record PayMealCommand(@NotNull @Valid MealPayment payment, @NotNull @TargetAggregateIdentifier String userId) {
}
