package com.wedoogift.depositapi.assertions.assertors;

import java.math.BigDecimal;

import org.assertj.core.api.AbstractObjectAssert;

import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;

/**
 * custom assertions for {@link Amount}
 */
public class AmountAssert extends AbstractObjectAssert<AmountAssert, Amount> {
    public AmountAssert(Amount amount) {
        super(amount, AmountAssert.class);
    }

    public AmountAssert hasCurrency(Currency expectedCurrency) {
        isNotNull();
        if (!actual.currency().equals(expectedCurrency)) {
            failWithMessage("expected currency must be equal to <%s> but was <%s>", expectedCurrency, actual.currency());
        }
        return myself;
    }

    public AmountAssert hasValue(BigDecimal expectedValue) {
        isNotNull();
        if (!actual.value().equals(expectedValue)) {
            failWithMessage("expected value must be equal to <%s> but was <%s>", expectedValue, actual.value());
        }
        return myself;
    }
}
