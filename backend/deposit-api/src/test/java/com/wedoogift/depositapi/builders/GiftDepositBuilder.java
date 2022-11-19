package com.wedoogift.depositapi.builders;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;

/**
 * object mother fluent builder for {@link com.wedoogift.depositapi.domain.entities.GiftDeposit}
 */
public class GiftDepositBuilder extends AbstractDepositBuilder<GiftDeposit, GiftDepositBuilder> {
    /**
     * private constructor
     */
    private GiftDepositBuilder() {
        super(new GiftDeposit());
    }

    public static GiftDepositBuilder one() {
        return new GiftDepositBuilder();
    }

    public static GiftDepositBuilder teslaDepositForJohn() {
        return one()
                .withAmount(BigDecimal.valueOf(100), Currency.EURO)
                .withCreationDate(LocalDate.of(2021, 6, 15))
                .withExpirationDate(LocalDate.of(2022, 6, 14));
    }
}
