package com.wedoogift.depositapi.services;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.wedoogift.depositapi.builders.MealDepositBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * tests of {@link MealExpirationDateService}
 */
class MealExpirationDateServiceTest {

    @Test
    @DisplayName("get should handle null values")
    void get_should_handle_null_values() {
        // GIVEN
        var mealExpirationDateService = new MealExpirationDateService();

        // WHEN
        var result = mealExpirationDateService.get(null);

        // THEN
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("get should compute meal expiration dates")
    void get_should_compute_expiration_date() {
        // GIVEN
        var mealExpirationDateService = new MealExpirationDateService();
        var mealDeposit = MealDepositBuilder.appleDepositForJessica().build();

        // WHEN
        var result = mealExpirationDateService.get(mealDeposit);

        // THEN
        assertThat(result).contains(LocalDate.of(2021, 2, 28));
    }
}