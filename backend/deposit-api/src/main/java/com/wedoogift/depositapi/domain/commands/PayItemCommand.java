package com.wedoogift.depositapi.domain.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractPayment;

public record PayItemCommand(AbstractPayment payment, @TargetAggregateIdentifier String userId) {
}
