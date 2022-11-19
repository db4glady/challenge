package com.wedoogift.depositapi.domain.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.wedoogift.depositapi.domain.entities.Amount;

public record CompanyMoneyTransferCommand(Amount amount, @TargetAggregateIdentifier String companyId) {
}
