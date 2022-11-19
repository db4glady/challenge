package com.wedoogift.depositapi.builders;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wedoogift.depositapi.domain.entities.AbstractPayment;
import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;

/**
 * abstract builder for {@link com.wedoogift.depositapi.domain.entities.GiftPayment} and {@link com.wedoogift.depositapi.domain.entities.MealPayment}
 *
 * @param <T> the payment type
 * @param <B> the builder type
 */
public abstract class AbstractPaymentBuilder<T extends AbstractPayment, B extends AbstractPaymentBuilder<T, B>> {

    final T actual;

    AbstractPaymentBuilder(T actual) {
        this.actual = actual;
    }

    public T build() {
        return actual;
    }

    public B withAmount(BigDecimal value, Currency currency) {
        actual.setAmount(new Amount(value, currency));
        return (B) this;
    }

    public B withAmount(Amount amount) {
        actual.setAmount(amount);
        return (B) this;
    }

    public B withDate(LocalDate localDate) {
        actual.setDate(localDate);
        return (B) this;
    }
}
