package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.PayGiftCommand;
import com.wedoogift.depositapi.domain.entities.GiftPayment;

/**
 * builder for {@link PayGiftCommand}
 */
public class PayGiftCommandBuilder {

    private String userId;
    private GiftPayment payment;

    /**
     * private constructor
     */
    private PayGiftCommandBuilder() {
    }

    public static PayGiftCommandBuilder one() {
        return new PayGiftCommandBuilder();
    }

    public static PayGiftCommandBuilder oneEuroJohnPaymentCommand() {
        return one()
                .withPayment(GiftPaymentBuilder.oneEuroPayment().build())
                .withUserId("John");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public PayGiftCommand build() {
        return new PayGiftCommand(payment, userId);
    }

    public PayGiftCommandBuilder withPayment(GiftPayment payment) {
        this.payment = payment;
        return this;
    }

    public PayGiftCommandBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
