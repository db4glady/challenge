package com.wedoogift.depositapi.domain.entities;

import java.time.LocalDate;

public abstract sealed class AbstractPayment permits GiftPayment, MealPayment {
    private Amount amount;
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
