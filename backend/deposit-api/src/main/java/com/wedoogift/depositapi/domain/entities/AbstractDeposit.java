package com.wedoogift.depositapi.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wedoogift.depositapi.services.ExpirationDateVisitor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GiftDeposit.class, name = "gift"),
        @JsonSubTypes.Type(value = MealDeposit.class, name = "meal")
})
public abstract sealed class AbstractDeposit permits MealDeposit, GiftDeposit {
    @NotNull
    private Amount amount;
    private Amount used;
    @NotNull
    private LocalDate creationDate;
    private LocalDate expirationDate;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
        Optional.ofNullable(amount)
                .map(a -> new Amount(BigDecimal.ZERO, a.currency()))
                .ifPresent(this::setUsed);
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Amount getUsed() {
        return used;
    }

    public void setUsed(Amount used) {
        this.used = used;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isAvailable() {
        if (amount == null || used == null) {
            return false;
        }
        return amount.value().compareTo(used.value()) > 0;
    }

    public Amount getBalance() {
        return amount.substract(used);
    }

    @JsonIgnore
    public abstract void accept(ExpirationDateVisitor expirationDateVisitor);
}
