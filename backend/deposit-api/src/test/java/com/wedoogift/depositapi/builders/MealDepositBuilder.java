package com.wedoogift.depositapi.builders;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.domain.entities.MealDeposit;

/**
 * object mother fluent builder for {@link MealDeposit}
 */
public class MealDepositBuilder extends AbstractDepositBuilder<MealDeposit, MealDepositBuilder> {
    /**
     * private constructor
     */
    private MealDepositBuilder() {
        super(new MealDeposit());
    }

    public static MealDepositBuilder one() {
        return new MealDepositBuilder();
    }

    public static MealDepositBuilder appleDepositForJessica() {
        return one()
                .withAmount(BigDecimal.valueOf(50), Currency.EURO)
                .withCreationDate(LocalDate.of(2020, 1, 1))
                .withExpirationDate(LocalDate.of(2021, 2, 28));
    }
}
