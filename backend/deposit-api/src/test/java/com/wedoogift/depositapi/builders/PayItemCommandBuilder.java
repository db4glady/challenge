package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.PayItemCommand;
import com.wedoogift.depositapi.domain.entities.AbstractPayment;

/**
 * builder for {@link PayItemCommand}
 */
public class PayItemCommandBuilder {

    private String userId;
    private AbstractPayment payment;

    /**
     * private constructor
     */
    private PayItemCommandBuilder() {
    }

    public static PayItemCommandBuilder one() {
        return new PayItemCommandBuilder();
    }

    public static PayItemCommandBuilder oneEuroJohnPaymentCommand() {
        return one()
                .withPayment(GiftPaymentBuilder.oneEuroPayment().build())
                .withUserId("John");
    }

    public static PayItemCommandBuilder oneEuroJessicaPaymentCommand() {
        return one()
                .withPayment(MealPaymentBuilder.oneEuroPayment().build())
                .withUserId("Jessica");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public PayItemCommand build() {
        return new PayItemCommand(payment, userId);
    }

    public PayItemCommandBuilder withPayment(AbstractPayment payment) {
        this.payment = payment;
        return this;
    }

    public PayItemCommandBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
