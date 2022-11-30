package com.wedoogift.depositapi.services;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wedoogift.depositapi.assertions.DepositApiAssertions;
import com.wedoogift.depositapi.builders.GiftDepositBuilder;
import com.wedoogift.depositapi.builders.MealDepositBuilder;
import com.wedoogift.depositapi.log.LogSpyExtension;
import com.wedoogift.depositapi.log.SpyLogAppender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * tests of {@link ConcreteExpirationDateVisitor}
 */
@ExtendWith({ MockitoExtension.class, LogSpyExtension.class })
class ConcreteExpirationDateVisitorTest {

    @SpyLogAppender(logger = "com.wedoogift.depositapi")
    ListAppender<ILoggingEvent> logAppender;

    @Mock
    MealExpirationDateService mealExpirationDateService;

    @Mock
    GiftExpirationDateService giftExpirationDateService;

    ConcreteExpirationDateVisitor expirationDateVisitor;

    @BeforeEach
    void setup() {
        expirationDateVisitor = new ConcreteExpirationDateVisitor(giftExpirationDateService, mealExpirationDateService);
    }

    @DisplayName("visit gift should call gift service")
    @Test
    void visit_gift_should_call_gift_service() {
        // GIVEN
        var giftDeposit = GiftDepositBuilder.teslaDepositForJohn().build();

        var expirationDate = LocalDate.now();
        given(giftExpirationDateService.get(giftDeposit)).willReturn(Optional.of(expirationDate));

        // WHEN
        expirationDateVisitor.visit(giftDeposit);

        // THEN
        then(giftExpirationDateService).should().get(giftDeposit);
        assertThat(giftDeposit.getExpirationDate()).isEqualTo(expirationDate);
        DepositApiAssertions.assertThat(logAppender).waitUntilLogFound(Level.INFO, "compute expiration date for gift deposit");
    }

    @DisplayName("visit meal should call meal service")
    @Test
    void visit_meal_should_call_meal_service() {
        // GIVEN
        var mealDeposit = MealDepositBuilder.appleDepositForJessica().build();

        var expirationDate = LocalDate.now();
        given(mealExpirationDateService.get(mealDeposit)).willReturn(Optional.of(expirationDate));

        // WHEN
        expirationDateVisitor.visit(mealDeposit);

        // THEN
        then(mealExpirationDateService).should().get(mealDeposit);
        assertThat(mealDeposit.getExpirationDate()).isEqualTo(expirationDate);
        DepositApiAssertions.assertThat(logAppender).waitUntilLogFound(Level.INFO, "compute expiration date for meal deposit");
    }
}