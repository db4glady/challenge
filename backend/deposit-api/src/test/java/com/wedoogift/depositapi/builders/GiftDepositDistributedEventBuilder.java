package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.entities.GiftDeposit;
import com.wedoogift.depositapi.domain.events.UserGiftDepositDistributedEvent;

/**
 * builder for {@link UserGiftDepositDistributedEvent}
 */
public class GiftDepositDistributedEventBuilder {

    private GiftDeposit deposit;
    private String userId;

    /**
     * private constructor
     */
    private GiftDepositDistributedEventBuilder() {
    }

    public static GiftDepositDistributedEventBuilder one() {
        return new GiftDepositDistributedEventBuilder();
    }

    public static GiftDepositDistributedEventBuilder teslaDepositDistributedEventForJohn() {
        return one()
                .withDeposit(GiftDepositBuilder.teslaDepositForJohn().build())
                .withUserId("John");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public UserGiftDepositDistributedEvent build() {
        return new UserGiftDepositDistributedEvent(deposit, userId);
    }

    public GiftDepositDistributedEventBuilder withDeposit(GiftDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public GiftDepositDistributedEventBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
