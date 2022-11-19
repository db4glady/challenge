package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;
import com.wedoogift.depositapi.domain.events.UserDepositDistributedEvent;

/**
 * builder for {@link UserDepositDistributedEvent}
 */
public class DepositDistributedEventBuilder {

    private AbstractDeposit deposit;
    private String userId;

    /**
     * private constructor
     */
    private DepositDistributedEventBuilder() {
    }

    public static DepositDistributedEventBuilder one() {
        return new DepositDistributedEventBuilder();
    }

    public static DepositDistributedEventBuilder teslaDepositDistributedEventForJohn() {
        return one()
                .withDeposit(GiftDepositBuilder.teslaDepositForJohn().build())
                .withUserId("John");
    }

    public static DepositDistributedEventBuilder appleDepositDistributedEventForJessica() {
        return one()
                .withDeposit(MealDepositBuilder.appleDepositForJessica().build())
                .withUserId("Jessica");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public UserDepositDistributedEvent build() {
        return new UserDepositDistributedEvent(deposit, userId);
    }

    public DepositDistributedEventBuilder withDeposit(AbstractDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public DepositDistributedEventBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
