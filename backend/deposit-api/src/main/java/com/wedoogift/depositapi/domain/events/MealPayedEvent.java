package com.wedoogift.depositapi.domain.events;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.MealPayment;

public record MealPayedEvent(@NotNull @Valid MealPayment payment, @NotNull @TargetAggregateIdentifier String userId) {
}
