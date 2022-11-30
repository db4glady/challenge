package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.entities.MealDeposit;
import com.wedoogift.depositapi.domain.events.UserMealDepositDistributedEvent;

/**
 * builder for {@link UserMealDepositDistributedEvent}
 */
public class MealDepositDistributedEventBuilder {

    private MealDeposit deposit;
    private String userId;

    /**
     * private constructor
     */
    private MealDepositDistributedEventBuilder() {
    }

    public static MealDepositDistributedEventBuilder one() {
        return new MealDepositDistributedEventBuilder();
    }

    public static MealDepositDistributedEventBuilder appleDepositDistributedEventForJessica() {
        return one()
                .withDeposit(MealDepositBuilder.appleDepositForJessica().build())
                .withUserId("Jessica");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public UserMealDepositDistributedEvent build() {
        return new UserMealDepositDistributedEvent(deposit, userId);
    }

    public MealDepositDistributedEventBuilder withDeposit(MealDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public MealDepositDistributedEventBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
