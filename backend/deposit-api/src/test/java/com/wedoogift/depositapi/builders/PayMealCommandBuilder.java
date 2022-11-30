package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.PayMealCommand;
import com.wedoogift.depositapi.domain.entities.MealPayment;

/**
 * builder for {@link PayMealCommand}
 */
public class PayMealCommandBuilder {

    private String userId;
    private MealPayment payment;

    /**
     * private constructor
     */
    private PayMealCommandBuilder() {
    }

    public static PayMealCommandBuilder one() {
        return new PayMealCommandBuilder();
    }

    public static PayMealCommandBuilder oneEuroJessicaPaymentCommand() {
        return one()
                .withPayment(MealPaymentBuilder.oneEuroPayment().build())
                .withUserId("Jessica");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public PayMealCommand build() {
        return new PayMealCommand(payment, userId);
    }

    public PayMealCommandBuilder withPayment(MealPayment payment) {
        this.payment = payment;
        return this;
    }

    public PayMealCommandBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
