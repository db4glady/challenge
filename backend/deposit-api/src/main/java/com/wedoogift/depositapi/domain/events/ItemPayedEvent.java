package com.wedoogift.depositapi.domain.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractPayment;

public record ItemPayedEvent(AbstractPayment payment, @TargetAggregateIdentifier String userId) {
}
