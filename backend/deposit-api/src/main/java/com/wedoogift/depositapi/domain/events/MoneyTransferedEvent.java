package com.wedoogift.depositapi.domain.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.Amount;

public record MoneyTransferedEvent(Amount amount, @TargetAggregateIdentifier String companyId) {
}
