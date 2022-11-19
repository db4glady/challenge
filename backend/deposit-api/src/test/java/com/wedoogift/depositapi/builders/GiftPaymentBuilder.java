package com.wedoogift.depositapi.builders;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.domain.entities.GiftPayment;

/**
 * object mother fluent builder for {@link GiftPayment}
 */
public class GiftPaymentBuilder extends AbstractPaymentBuilder<GiftPayment, GiftPaymentBuilder> {
    /**
     * private constructor
     */
    private GiftPaymentBuilder() {
        super(new GiftPayment());
    }

    public static GiftPaymentBuilder one() {
        return new GiftPaymentBuilder();
    }

    public static GiftPaymentBuilder oneEuroPayment() {
        return one()
                .withAmount(BigDecimal.ONE, Currency.EURO)
                .withDate(LocalDate.of(2022, 1, 2));
    }

    public static GiftPaymentBuilder tenEurosPayment() {
        return one()
                .withAmount(BigDecimal.TEN, Currency.EURO)
                .withDate(LocalDate.of(2022, 1, 2));
    }

    public static GiftPaymentBuilder bigPayment() {
        return one()
                .withAmount(BigDecimal.valueOf(150), Currency.EURO)
                .withDate(LocalDate.of(2022, 1, 2));
    }
}
