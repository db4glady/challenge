package com.wedoogift.depositapi.domain.entities;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public abstract sealed class AbstractPayment permits GiftPayment, MealPayment {
    @NotNull @Valid
    private Amount amount;
    @NotNull
    private LocalDate date;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
