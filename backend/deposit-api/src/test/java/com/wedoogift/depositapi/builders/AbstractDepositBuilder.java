package com.wedoogift.depositapi.builders;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;
import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;

/**
 * abstract builder for {@link com.wedoogift.depositapi.domain.entities.GiftDeposit} and {@link com.wedoogift.depositapi.domain.entities.MealDeposit}
 *
 * @param <T> the deposit type
 * @param <B> the builder type
 */
public abstract class AbstractDepositBuilder<T extends AbstractDeposit, B extends AbstractDepositBuilder<T, B>> {

    final T actual;

    AbstractDepositBuilder(T actual) {
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

    public B withCreationDate(LocalDate localDate) {
        actual.setCreationDate(localDate);
        return (B) this;
    }

    public B withExpirationDate(LocalDate localDate) {
        actual.setExpirationDate(localDate);
        return (B) this;
    }
}
