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

import com.wedoogift.depositapi.domain.commands.DistributeGiftDepositForUserCommand;
import com.wedoogift.depositapi.domain.commands.DistributeMealDepositForUserCommand;
import com.wedoogift.depositapi.domain.commands.PayGiftCommand;
import com.wedoogift.depositapi.domain.commands.PayMealCommand;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;
import com.wedoogift.depositapi.domain.entities.MealDeposit;
import com.wedoogift.depositapi.domain.events.GiftPayedEvent;
import com.wedoogift.depositapi.domain.events.MealPayedEvent;
import com.wedoogift.depositapi.domain.events.UserGiftDepositDistributedEvent;
import com.wedoogift.depositapi.domain.events.UserMealDepositDistributedEvent;
import com.wedoogift.depositapi.services.ExpirationDateVisitor;
import com.wedoogift.depositapi.services.UserPaymentService;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * the aggregate used to compute the user balance
 */
@Aggregate(snapshotTriggerDefinition = "userSnapshotTrigger")
public class UserAggregate {

    private List<MealDeposit> mealDeposits = new ArrayList<>();
    private List<GiftDeposit> giftDeposits = new ArrayList<>();
    @AggregateIdentifier
    private String userId;

    public List<MealDeposit> getMealDeposits() {
        return mealDeposits;
    }

    public void setMealDeposits(List<MealDeposit> mealDeposits) {
        this.mealDeposits = mealDeposits;
    }

    public List<GiftDeposit> getGiftDeposits() {
        return giftDeposits;
    }

    public void setGiftDeposits(List<GiftDeposit> giftDeposits) {
        this.giftDeposits = giftDeposits;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * {@link DistributeGiftDepositForUserCommand} handler used to receive deposits
     *
     * @param distributeDepositForUserCommand the command to handle
     * @param expirationDateVisitor           the service computing the expiration date
     */
    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(DistributeGiftDepositForUserCommand distributeDepositForUserCommand,
            ExpirationDateVisitor expirationDateVisitor) {
        var deposit = distributeDepositForUserCommand.deposit();
        // validation
        Assert.notNull(deposit, () -> "the command deposit should not be null");
        Assert.notNull(deposit.getCreationDate(), () -> "the command deposit creation date should not be null");
        Assert.notNull(deposit.getAmount(), () -> "the command deposit amount should not be null");
        Assert.isTrue(distributeDepositForUserCommand.userId() != null && !"".equals(distributeDepositForUserCommand.userId()),
                () -> "the command user id should not be null or empty");
        // apply event
        deposit.accept(expirationDateVisitor);

        apply(new UserGiftDepositDistributedEvent(
                deposit,
                distributeDepositForUserCommand.userId()));
    }

    /**
     * {@link DistributeMealDepositForUserCommand} handler used to receive deposits
     *
     * @param distributeDepositForUserCommand the command to handle
     * @param expirationDateVisitor           the service computing the expiration date
     */
    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(DistributeMealDepositForUserCommand distributeDepositForUserCommand, ExpirationDateVisitor expirationDateVisitor) {
        var deposit = distributeDepositForUserCommand.deposit();
        // validation
        Assert.notNull(deposit, () -> "the command deposit should not be null");
        Assert.notNull(deposit.getCreationDate(), () -> "the command deposit creation date should not be null");
        Assert.notNull(deposit.getAmount(), () -> "the command deposit amount should not be null");
        Assert.isTrue(distributeDepositForUserCommand.userId() != null && !"".equals(distributeDepositForUserCommand.userId()),
                () -> "the command user id should not be null or empty");
        // apply event
        deposit.accept(expirationDateVisitor);

        apply(new UserMealDepositDistributedEvent(
                deposit,
                distributeDepositForUserCommand.userId()));
    }

    /**
     * {@link PayGiftCommand} handler used to pay gift or meal
     *
     * @param payGiftCommand the command to handle
     */
    @CommandHandler
    public void handle(PayGiftCommand payGiftCommand, UserPaymentService userPaymentService) {
        // validation
        Assert.notNull(payGiftCommand.payment(), () -> "the command payment should not be null");
        Assert.notNull(payGiftCommand.payment().getAmount(), () -> "the command payment amount should not be null");
        Assert.notNull(payGiftCommand.payment().getDate(), () -> "the command payment date should not be null");

        Assert.isTrue(userPaymentService.getBalance(
                                giftDeposits,
                                payGiftCommand.payment().getDate(),
                                payGiftCommand.payment().getAmount().currency())
                        .value().compareTo(BigDecimal.ZERO) > 0,
                () -> "insufficient balance");
        // apply event
        apply(new GiftPayedEvent(payGiftCommand.payment(), payGiftCommand.userId()));
    }

    /**
     * {@link PayMealCommand} handler used to pay gift or meal
     *
     * @param payMealCommand the command to handle
     */
    @CommandHandler
    public void handle(PayMealCommand payMealCommand, UserPaymentService userPaymentService) {
        // validation
        Assert.notNull(payMealCommand.payment(), () -> "the command payment should not be null");
        Assert.notNull(payMealCommand.payment().getAmount(), () -> "the command payment amount should not be null");
        Assert.notNull(payMealCommand.payment().getDate(), () -> "the command payment date should not be null");

        Assert.isTrue(userPaymentService.getBalance(
                                mealDeposits,
                                payMealCommand.payment().getDate(),
                                payMealCommand.payment().getAmount().currency())
                        .value().compareTo(BigDecimal.ZERO) > 0,
                () -> "insufficient balance");
        // apply event
        apply(new MealPayedEvent(payMealCommand.payment(), payMealCommand.userId()));
    }

    /**
     * event received when a deposit has been done by a company
     *
     * @param userDepositDistributedEvent the event received
     */
    @EventSourcingHandler
    public void on(UserMealDepositDistributedEvent userDepositDistributedEvent) {
        userId = userDepositDistributedEvent.userId();
        mealDeposits.add(userDepositDistributedEvent.deposit());
    }

    /**
     * event received when a deposit has been done by a company
     *
     * @param userDepositDistributedEvent the event received
     */
    @EventSourcingHandler
    public void on(UserGiftDepositDistributedEvent userDepositDistributedEvent) {
        userId = userDepositDistributedEvent.userId();
        giftDeposits.add(userDepositDistributedEvent.deposit());
    }

    /**
     * event received when a meal payment has been done by a user
     *
     * @param mealPayedEvent     the event received
     * @param userPaymentService the service used to add the payment to the right deposits
     */
    @EventSourcingHandler
    public void on(MealPayedEvent mealPayedEvent, UserPaymentService userPaymentService) {
        userPaymentService.pay(mealDeposits, mealPayedEvent.payment());
    }

    /**
     * event received when a gift payment has been done by a user
     *
     * @param giftPayedEvent     the event received
     * @param userPaymentService the service used to add the payment to the right deposits
     */
    @EventSourcingHandler
    public void on(GiftPayedEvent giftPayedEvent, UserPaymentService userPaymentService) {
        userPaymentService.pay(giftDeposits, giftPayedEvent.payment());
    }
}
