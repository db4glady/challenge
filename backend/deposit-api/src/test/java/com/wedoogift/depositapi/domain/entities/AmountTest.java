package com.wedoogift.depositapi.domain.entities;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.wedoogift.depositapi.assertions.DepositApiAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * test de {@link Amount}
 */
class AmountTest {

    @DisplayName("plus should add amounts when currencies are equal")
    @Test
    void plus_should_add_amounts_when_currencies_are_equal() {
        // GIVEN
        var firstAmount = new Amount(BigDecimal.TEN, Currency.EURO);
        var secondAmount = new Amount(BigDecimal.ONE, Currency.EURO);

        // WHEN
        var result = firstAmount.plus(secondAmount);

        // THEN
        assertThat(result)
                .hasValue(BigDecimal.valueOf(11))
                .hasCurrency(Currency.EURO);
    }

    @DisplayName("plus should add amounts when currencies are different")
    @Test
    void plus_should_throw_exception_when_currencies_are_different() {
        // GIVEN
        var firstAmount = new Amount(BigDecimal.TEN, Currency.EURO);
        var secondAmount = new Amount(BigDecimal.ONE, Currency.DOLLAR);

        // WHEN THEN
        assertThrows(UnsupportedOperationException.class, () -> firstAmount.plus(secondAmount));
    }

    @DisplayName("plus should add amounts")
    @Test
    void plus_should_add_amounts() {
        // GIVEN
        var firstAmount = new Amount(BigDecimal.TEN, Currency.EURO);
        var secondAmount = BigDecimal.ONE;

        // WHEN
        var result = firstAmount.plus(secondAmount);

        // THEN
        assertThat(result)
                .hasValue(BigDecimal.valueOf(11))
                .hasCurrency(Currency.EURO);
    }

    @DisplayName("substract should substract amounts when currencies are equal")
    @Test
    void substract_should_substract_amounts_when_currencies_are_equal() {
        // GIVEN
        var firstAmount = new Amount(BigDecimal.TEN, Currency.EURO);
        var secondAmount = new Amount(BigDecimal.ONE, Currency.EURO);

        // WHEN
        var result = firstAmount.substract(secondAmount);

        // THEN
        assertThat(result)
                .hasValue(BigDecimal.valueOf(9))
                .hasCurrency(Currency.EURO);
    }

    @DisplayName("substract should substract amounts")
    @Test
    void substract_should_substract_amounts() {
        // GIVEN
        var firstAmount = new Amount(BigDecimal.TEN, Currency.EURO);
        var secondAmount = BigDecimal.ONE;

        // WHEN
        var result = firstAmount.substract(secondAmount);

        // THEN
        assertThat(result)
                .hasValue(BigDecimal.valueOf(9))
                .hasCurrency(Currency.EURO);
    }

    @DisplayName("substract should add amounts when currencies are different")
    @Test
    void substract_should_throw_exception_when_currencies_are_different() {
        // GIVEN
        var firstAmount = new Amount(BigDecimal.TEN, Currency.EURO);
        var secondAmount = new Amount(BigDecimal.ONE, Currency.DOLLAR);

        // WHEN THEN
        assertThrows(UnsupportedOperationException.class, () -> firstAmount.substract(secondAmount));
    }
}