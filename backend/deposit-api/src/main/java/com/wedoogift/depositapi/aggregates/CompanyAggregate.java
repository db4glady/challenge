package com.wedoogift.depositapi.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import com.wedoogift.depositapi.domain.commands.CompanyMoneyTransferCommand;
import com.wedoogift.depositapi.domain.commands.DistributeDepositForUserCommand;
import com.wedoogift.depositapi.domain.commands.DistributeDepositFromCompanyCommand;
import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.domain.events.CompanyDepositDistributedEvent;
import com.wedoogift.depositapi.domain.events.MoneyTransferedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * Aggregate used to compute the company balance
 */
@Aggregate
public class CompanyAggregate {

    @AggregateIdentifier
    private String companyId;
    private Amount balance = new Amount(BigDecimal.ZERO, Currency.EURO);

    /**
     * {@link DistributeDepositForUserCommand} handler used to transfer money to the company account
     *
     * @param companyMoneyTransferCommand the command to handle
     */
    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(CompanyMoneyTransferCommand companyMoneyTransferCommand) {
        // validation
        Assert.notNull(companyMoneyTransferCommand.amount(), () -> "the command transfer amount should not be null");
        Assert.isTrue(companyMoneyTransferCommand.companyId() != null && !"".equals(companyMoneyTransferCommand.companyId()),
                () -> "the command company id should not be null or empty");
        // apply event
        apply(new MoneyTransferedEvent(
                companyMoneyTransferCommand.amount(),
                companyMoneyTransferCommand.companyId()));
    }

    /**
     * {@link DistributeDepositFromCompanyCommand} used when the company distribute gifts or meal to the users
     *
     * @param distributeDepositFromCompanyCommand the command
     */
    @CommandHandler
    public void handle(DistributeDepositFromCompanyCommand distributeDepositFromCompanyCommand) {
        var deposit = distributeDepositFromCompanyCommand.deposit();
        // validation
        Assert.notNull(deposit.getAmount(), () -> "the command deposit amount should not be null");
        Assert.isTrue(distributeDepositFromCompanyCommand.companyId() != null && !"".equals(distributeDepositFromCompanyCommand.companyId()),
                () -> "the command company id should not be null or empty");
        Assert.isTrue(balance.currency().equals(deposit.getAmount().currency()),
                () -> "the company account does not support this currency");
        Assert.isTrue(balance.value().compareTo(deposit.getAmount().value()) >= 0,
                () -> "the company balance is not sufficient");
        // apply event
        apply(new CompanyDepositDistributedEvent(
                deposit.getAmount(),
                distributeDepositFromCompanyCommand.companyId()));
    }

    /**
     * event received when a money transfer is done
     *
     * @param moneyTransferedEvent the event received
     */
    @EventSourcingHandler
    public void on(MoneyTransferedEvent moneyTransferedEvent) {
        companyId = moneyTransferedEvent.companyId();
        balance = balance.plus(moneyTransferedEvent.amount());
    }

    /**
     * event received when a comany make a deposit to a user
     *
     * @param companyDepositDistributedEvent the event received
     */
    @EventSourcingHandler
    public void on(CompanyDepositDistributedEvent companyDepositDistributedEvent) {
        companyId = companyDepositDistributedEvent.companyId();
        balance = balance.substract(companyDepositDistributedEvent.amount());
    }
}
