package com.wedoogift.depositapi.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wedoogift.depositapi.builders.GiftDepositBuilder;
import com.wedoogift.depositapi.builders.GiftPaymentBuilder;
import com.wedoogift.depositapi.domain.entities.Currency;

import static com.wedoogift.depositapi.assertions.DepositApiAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * tests of {@link UserPaymentService}
 */
class UserPaymentServiceTest {

    @Test
    @DisplayName("getBalance should include start date deposit")
    void getBalance_should_compute_should_include_start_date_deposit() {
        // GIVEN
        var userPaymentService = new UserPaymentService();
        var deposits = List.of(
                GiftDepositBuilder.teslaDepositForJohn().build()
        );
        var date = LocalDate.of(2021, 6, 15);

        // WHEN
        var balance = userPaymentService.getBalance(deposits, date, Currency.EURO);

        // THEN
        assertThat(balance)
                .hasCurrency(Currency.EURO)
                .hasValue(BigDecimal.valueOf(100));
    }

    @Test
    @DisplayName("getBalance should include expiration date deposit")
    void getBalance_should_compute_should_include_expiration_date_deposit() {
        // GIVEN
        var userPaymentService = new UserPaymentService();
        var deposits = List.of(
                GiftDepositBuilder.teslaDepositForJohn().build()
        );
        var date = LocalDate.of(2022, 6, 14);

        // WHEN
        var balance = userPaymentService.getBalance(deposits, date, Currency.EURO);

        // THEN
        assertThat(balance)
                .hasCurrency(Currency.EURO)
                .hasValue(BigDecimal.valueOf(100));
    }

    @Test
    @DisplayName("getBalance should filter other currencies deposits")
    void getBalance_should_compute_should_filter_other_currencies_deposits() {
        // GIVEN
        var userPaymentService = new UserPaymentService();
        var deposits = List.of(
                GiftDepositBuilder.teslaDepositForJohn().build()
        );
        var date = LocalDate.of(2022, 6, 14);

        // WHEN
        var balance = userPaymentService.getBalance(deposits, date, Currency.DOLLAR);

        // THEN
        assertThat(balance)
                .hasCurrency(Currency.DOLLAR)
                .hasValue(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("getBalance should add deposits")
    void getBalance_should_compute_should_add_deposits() {
        // GIVEN
        var userPaymentService = new UserPaymentService();
        var deposits = List.of(
                GiftDepositBuilder.teslaDepositForJohn().build(),
                GiftDepositBuilder.teslaDepositForJohn().build()
        );
        var date = LocalDate.of(2022, 6, 1);

        // WHEN
        var balance = userPaymentService.getBalance(deposits, date, Currency.EURO);

        // THEN
        assertThat(balance)
                .hasCurrency(Currency.EURO)
                .hasValue(BigDecimal.valueOf(200));
    }

    @Test
    @DisplayName("pay should distribute payments on valid payments")
    void pay_should_distribute_payments_on_valid_deposits() {
        // GIVEN
        var userPaymentService = new UserPaymentService();
        var firstGift = GiftDepositBuilder.teslaDepositForJohn().build();
        var secondGift = GiftDepositBuilder.teslaDepositForJohn()
                .withExpirationDate(firstGift.getExpirationDate().plusDays(1)).build();
        var otherGift = GiftDepositBuilder.teslaDepositForJohn()
                .withCreationDate(LocalDate.of(2019, 6, 15))
                .withExpirationDate(LocalDate.of(2020, 6, 14)).build();
        var deposits = List.of(
                firstGift,
                secondGift,
                otherGift
        );

        var payment = GiftPaymentBuilder.bigPayment().build();

        // WHEN
        userPaymentService.pay(deposits, payment);

        // THEN
        assertThat(firstGift.getUsed())
                .hasCurrency(Currency.EURO)
                .hasValue(BigDecimal.valueOf(100));
        assertThat(secondGift.getUsed())
                .hasCurrency(Currency.EURO)
                .hasValue(BigDecimal.valueOf(50));
        assertThat(otherGift.getUsed())
                .hasCurrency(Currency.EURO)
                .hasValue(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("pay should throw exception if balance is insufficient")
    void pay_should_throw_exception_if_balance_is_insufficient() {
        // GIVEN
        var userPaymentService = new UserPaymentService();
        var deposits = List.of(GiftDepositBuilder.teslaDepositForJohn().build());

        var payment = GiftPaymentBuilder.bigPayment().build();

        // WHEN THEN
        assertThrows(IllegalStateException.class, () -> userPaymentService.pay(deposits, payment));
    }
}