package com.wedoogift.depositapi.domain.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;

public record UserDepositDistributedEvent(AbstractDeposit deposit, @TargetAggregateIdentifier String userId) {
}
