package com.wedoogift.depositapi.domain.entities;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public record Amount(@NotNull(message = "Amount value should not be null") BigDecimal value,
                     Currency currency) {

    public Amount plus(Amount amount) {
        if(!amount.currency.equals(currency)) {
            throw new UnsupportedOperationException("amounts of different currencies can not be added");
        }
        return new Amount(value.add(amount.value), currency);
    }

    public Amount plus(BigDecimal val) {
        return new Amount(value.add(val), currency);
    }

    public Amount substract(Amount amount) {
        if(!amount.currency.equals(currency)) {
            throw new UnsupportedOperationException("amounts of different currencies can not be added");
        }
        return new Amount(value.subtract(amount.value), currency);
    }

    public Amount substract(BigDecimal val) {
        return new Amount(value.subtract(val), currency);
    }
}
