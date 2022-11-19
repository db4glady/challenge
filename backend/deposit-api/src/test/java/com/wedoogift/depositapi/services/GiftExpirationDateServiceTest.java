package com.wedoogift.depositapi.services;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wedoogift.depositapi.builders.GiftDepositBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * tests of {@link GiftExpirationDateService}
 */
class GiftExpirationDateServiceTest {

    @Test
    @DisplayName("get should handle null values")
    void get_should_handle_null_values() {
        // GIVEN
        var giftExpirationDateService = new GiftExpirationDateService();

        // WHEN
        var result = giftExpirationDateService.get(null);

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("get should compute meal expiration dates")
    void get_should_compute_expiration_date() {
        // GIVEN
        var giftExpirationDateService = new GiftExpirationDateService();
        var giftDeposit = GiftDepositBuilder.teslaDepositForJohn().build();

        // WHEN
        var result = giftExpirationDateService.get(giftDeposit);

        // THEN
        assertThat(result).contains(LocalDate.of(2022, 6, 14));
    }
}