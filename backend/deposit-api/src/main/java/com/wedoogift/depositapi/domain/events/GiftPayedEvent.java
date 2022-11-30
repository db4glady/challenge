package com.wedoogift.depositapi.domain.events;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.GiftPayment;

public record GiftPayedEvent(@NotNull @Valid GiftPayment payment, @NotNull @TargetAggregateIdentifier String userId) {
}
