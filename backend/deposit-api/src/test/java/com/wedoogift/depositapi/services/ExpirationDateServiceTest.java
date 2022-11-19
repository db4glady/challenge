package com.wedoogift.depositapi.services;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wedoogift.depositapi.builders.GiftDepositBuilder;
import com.wedoogift.depositapi.builders.MealDepositBuilder;
import com.wedoogift.depositapi.log.LogSpyExtension;
import com.wedoogift.depositapi.log.SpyLogAppender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import static com.wedoogift.depositapi.assertions.DepositApiAssertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * test class of {@link ExpirationDateService}
 */
@ExtendWith({ MockitoExtension.class, LogSpyExtension.class })
class ExpirationDateServiceTest {

    @SpyLogAppender(logger = "com.wedoogift.depositapi")
    public ListAppender<ILoggingEvent> logAppender;

    @Mock
    MealExpirationDateService mealExpirationDateService;

    @Mock
    GiftExpirationDateService giftExpirationDateService;

    ExpirationDateService expirationDateService;

    @BeforeEach
    void setup() {
        // we don't use InjectMocks to check the constructor parameters at compile time
        expirationDateService = new ExpirationDateService(giftExpirationDateService, mealExpirationDateService);
    }

    @DisplayName("get should handle a null parameter")
    @Test
    void get_should_handle_null_deposit() {
        // GIVEN

        // WHEN
        var result = expirationDateService.get(null);

        // THEN
        assertThat(result).isEmpty();
    }

    @DisplayName("get should call the gift expiration date service")
    @Test
    void get_should_call_gift_service() {
        // GIVEN
        var deposit = GiftDepositBuilder.teslaDepositForJohn().build();
        var expirationDate = LocalDate.of(2022, 6, 14);

        given(giftExpirationDateService.get(deposit)).willReturn(Optional.of(expirationDate));

        // WHEN
        var result = expirationDateService.get(deposit);

        // THEN
        assertThat(result).contains(expirationDate);
        assertThat(logAppender).waitUntilLogFound(Level.INFO, "compute expiration date for gift deposit");
    }

    @DisplayName("get should call the meal expiration date service")
    @Test
    void get_should_call_meal_service() {
        // GIVEN
        var deposit = MealDepositBuilder.appleDepositForJessica().build();
        var expirationDate = LocalDate.of(2021, 2, 28);

        given(mealExpirationDateService.get(deposit)).willReturn(Optional.of(expirationDate));

        // WHEN
        var result = expirationDateService.get(deposit);

        // THEN
        assertThat(result).contains(expirationDate);
        assertThat(logAppender).waitUntilLogFound(Level.INFO, "compute expiration date for meal deposit");
    }
}