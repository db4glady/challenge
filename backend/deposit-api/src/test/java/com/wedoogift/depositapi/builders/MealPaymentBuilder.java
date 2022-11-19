package com.wedoogift.depositapi.builders;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.domain.entities.MealPayment;

/**
 * object mother fluent builder for {@link MealPayment}
 */
public class MealPaymentBuilder extends AbstractPaymentBuilder<MealPayment, MealPaymentBuilder> {
    /**
     * private constructor
     */
    private MealPaymentBuilder() {
        super(new MealPayment());
    }

    public static MealPaymentBuilder one() {
        return new MealPaymentBuilder();
    }

    public static MealPaymentBuilder oneEuroPayment() {
        return one()
                .withAmount(BigDecimal.ONE, Currency.EURO)
                .withDate(LocalDate.of(2022, 1, 2));
    }

    public static MealPaymentBuilder tenEurosPayment() {
        return one()
                .withAmount(BigDecimal.TEN, Currency.EURO)
                .withDate(LocalDate.of(2022, 1, 2));
    }

    public static MealPaymentBuilder bigPayment() {
        return one()
                .withAmount(BigDecimal.valueOf(150), Currency.EURO)
                .withDate(LocalDate.of(2022, 1, 2));
    }
}
