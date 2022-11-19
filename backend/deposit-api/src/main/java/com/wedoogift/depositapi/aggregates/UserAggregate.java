package com.wedoogift.depositapi.aggregates;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import com.wedoogift.depositapi.domain.commands.DistributeDepositForUserCommand;
import com.wedoogift.depositapi.domain.commands.PayItemCommand;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;
import com.wedoogift.depositapi.domain.entities.GiftPayment;
import com.wedoogift.depositapi.domain.entities.MealDeposit;
import com.wedoogift.depositapi.domain.entities.MealPayment;
import com.wedoogift.depositapi.domain.events.ItemPayedEvent;
import com.wedoogift.depositapi.domain.events.UserDepositDistributedEvent;
import com.wedoogift.depositapi.services.ExpirationDateService;
import com.wedoogift.depositapi.services.UserPaymentService;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * the aggregate used to compute the user balance
 */
@Aggregate
public class UserAggregate {

    private final List<MealDeposit> mealDeposits = new ArrayList<>();
    private final List<GiftDeposit> giftDeposits = new ArrayList<>();
    @AggregateIdentifier
    private String userId;

    /**
     * {@link DistributeDepositForUserCommand} handler used to receive deposits
     *
     * @param distributeDepositForUserCommand the command to handle
     * @param expirationDateService           the service computing the expiration date
     */
    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(DistributeDepositForUserCommand distributeDepositForUserCommand, ExpirationDateService expirationDateService) {
        var deposit = distributeDepositForUserCommand.deposit();
        // validation
        Assert.notNull(deposit, () -> "the command deposit should not be null");
        Assert.notNull(deposit.getCreationDate(), () -> "the command deposit creation date should not be null");
        Assert.notNull(deposit.getAmount(), () -> "the command deposit amount should not be null");
        Assert.isTrue(distributeDepositForUserCommand.userId() != null && !"".equals(distributeDepositForUserCommand.userId()),
                () -> "the command user id should not be null or empty");
        // apply event
        deposit.setExpirationDate(expirationDateService.get(deposit)
                .orElseThrow(() -> new IllegalArgumentException("expiration date should not be null")));
        apply(new UserDepositDistributedEvent(
                deposit,
                distributeDepositForUserCommand.userId()));
    }

    /**
     * {@link PayItemCommand} handler used to pay gift or meal
     *
     * @param payItemCommand the command to handle
     */
    @CommandHandler
    public void handle(PayItemCommand payItemCommand, UserPaymentService userPaymentService) {
        // validation
        Assert.notNull(payItemCommand.payment(), () -> "the command payment should not be null");
        Assert.notNull(payItemCommand.payment().getAmount(), () -> "the command payment amount should not be null");
        Assert.notNull(payItemCommand.payment().getDate(), () -> "the command payment date should not be null");
        if (payItemCommand.payment() instanceof MealPayment) {
            Assert.isTrue(userPaymentService.getBalance(
                                    mealDeposits,
                                    payItemCommand.payment().getDate(),
                                    payItemCommand.payment().getAmount().currency())
                            .value().compareTo(BigDecimal.ZERO) > 0,
                    () -> "insufficient balance");
        } else if (payItemCommand.payment() instanceof GiftPayment) {
            Assert.isTrue(userPaymentService.getBalance(
                                    giftDeposits,
                                    payItemCommand.payment().getDate(),
                                    payItemCommand.payment().getAmount().currency())
                            .value().compareTo(BigDecimal.ZERO) > 0,
                    () -> "insufficient balance");
        }
        // apply event
        apply(new ItemPayedEvent(payItemCommand.payment(), payItemCommand.userId()));
    }

    /**
     * event received when a deposit has been done by a company
     *
     * @param userDepositDistributedEvent the event received
     */
    @EventSourcingHandler
    public void on(UserDepositDistributedEvent userDepositDistributedEvent) {
        userId = userDepositDistributedEvent.userId();
        var deposit = userDepositDistributedEvent.deposit();
        if (deposit instanceof MealDeposit mealDeposit) {
            mealDeposits.add(mealDeposit);
        } else if (deposit instanceof GiftDeposit giftDeposit) {
            giftDeposits.add(giftDeposit);
        }
    }

    /**
     * event received when a payment has been done by a user
     *
     * @param itemPayedEvent     the event received
     * @param userPaymentService the service used to add the payment to the right deposits
     */
    @EventSourcingHandler
    public void on(ItemPayedEvent itemPayedEvent, UserPaymentService userPaymentService) {
        if (itemPayedEvent.payment() instanceof MealPayment mealPayment) {
            userPaymentService.pay(mealDeposits, mealPayment);
        } else if (itemPayedEvent.payment() instanceof GiftPayment giftPayment) {
            userPaymentService.pay(giftDeposits, giftPayment);
        }
    }
}
