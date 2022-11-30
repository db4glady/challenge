package com.wedoogift.depositapi.aggregates;

import java.math.BigDecimal;
import java.util.stream.Stream;

import javax.validation.Validator;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wedoogift.depositapi.builders.DistributeGiftDepositForUserCommandBuilder;
import com.wedoogift.depositapi.builders.DistributeMealDepositForUserCommandBuilder;
import com.wedoogift.depositapi.builders.GiftDepositBuilder;
import com.wedoogift.depositapi.builders.GiftDepositDistributedEventBuilder;
import com.wedoogift.depositapi.builders.MealDepositBuilder;
import com.wedoogift.depositapi.builders.MealDepositDistributedEventBuilder;
import com.wedoogift.depositapi.builders.PayGiftCommandBuilder;
import com.wedoogift.depositapi.builders.PayMealCommandBuilder;
import com.wedoogift.depositapi.domain.commands.DistributeGiftDepositForUserCommand;
import com.wedoogift.depositapi.domain.commands.DistributeMealDepositForUserCommand;
import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.services.ExpirationDateVisitor;
import com.wedoogift.depositapi.services.UserPaymentService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * tests de {@link UserAggregate}
 */
@ExtendWith(MockitoExtension.class)
class UserAggregateTest {
    @Mock
    ExpirationDateVisitor expirationDateVisitor;

    @Mock
    UserPaymentService userPaymentService;

    UserAggregate userAggregate;

    FixtureConfiguration<UserAggregate> fixture;

    static Stream<Arguments> notValidMealDepositCommands() {
        return Stream.of(Arguments.of(
                DistributeMealDepositForUserCommandBuilder.appleDepositForJessicaCommand().withUserId(null).build(),
                DistributeMealDepositForUserCommandBuilder.appleDepositForJessicaCommand().withUserId("").build(),
                DistributeMealDepositForUserCommandBuilder.appleDepositForJessicaCommand().withDeposit(null).build(),
                DistributeMealDepositForUserCommandBuilder.appleDepositForJessicaCommand()
                        .withDeposit(MealDepositBuilder.appleDepositForJessica().withCreationDate(null).build()).build(),
                DistributeMealDepositForUserCommandBuilder.appleDepositForJessicaCommand()
                        .withDeposit(MealDepositBuilder.appleDepositForJessica().withAmount(null).build()).build()));
    }

    static Stream<Arguments> notValidGiftDepositCommands() {
        return Stream.of(Arguments.of(
                DistributeGiftDepositForUserCommandBuilder.teslaDepositForJohnCommand().withUserId(null).build(),
                DistributeGiftDepositForUserCommandBuilder.teslaDepositForJohnCommand().withUserId("").build(),
                DistributeGiftDepositForUserCommandBuilder.teslaDepositForJohnCommand().withDeposit(null).build(),
                DistributeGiftDepositForUserCommandBuilder.teslaDepositForJohnCommand()
                        .withDeposit(GiftDepositBuilder.teslaDepositForJohn().withCreationDate(null).build()).build(),
                DistributeGiftDepositForUserCommandBuilder.teslaDepositForJohnCommand()
                        .withDeposit(GiftDepositBuilder.teslaDepositForJohn().withAmount(null).build()).build()));
    }

    @BeforeEach
    void setup() {
        userAggregate = new UserAggregate();
        fixture = new AggregateTestFixture<>(UserAggregate.class)
                .registerInjectableResource(expirationDateVisitor)
                .registerInjectableResource(userPaymentService);
    }

    @ParameterizedTest
    @MethodSource({ "notValidMealDepositCommands" })
    @DisplayName("handle meal deposits should throw an IllegalArgumentException input data is not valid")
    void handle_meal_deposits_should_throw_exception_when_user_id_is_null(DistributeMealDepositForUserCommand command) {
        assertThrows(IllegalArgumentException.class, () -> userAggregate.handle(command, expirationDateVisitor));
    }

    @ParameterizedTest
    @MethodSource({ "notValidGiftDepositCommands" })
    @DisplayName("handle meal deposits should throw an IllegalArgumentException input data is not valid")
    void handle_gift_deposits_should_throw_exception_when_user_id_is_null(DistributeGiftDepositForUserCommand command) {
        assertThrows(IllegalArgumentException.class, () -> userAggregate.handle(command, expirationDateVisitor));
    }

    @Test
    @DisplayName("handle meal deposits should apply events")
    void handle_meal_deposits_should_apply_events() {
        var command = DistributeMealDepositForUserCommandBuilder.appleDepositForJessicaCommand().build();

        fixture.given()
                .when(command)
                .expectSuccessfulHandlerExecution();
    }

    @Test
    @DisplayName("handle gift deposits should apply events")
    void handle_gift_deposits_should_apply_events() {
        var command = DistributeGiftDepositForUserCommandBuilder.teslaDepositForJohnCommand().build();

        fixture.given()
                .when(command)
                .expectSuccessfulHandlerExecution();
    }

    @DisplayName("handle payments should apply events for gifts")
    @Test
    void handle_payments_should_apply_events_for_gift() {
        var previousEvent = GiftDepositDistributedEventBuilder.teslaDepositDistributedEventForJohn().build();
        var command = PayGiftCommandBuilder.oneEuroJohnPaymentCommand().build();

        given(userPaymentService.getBalance(anyList(), eq(command.payment().getDate()), eq(Currency.EURO)))
                .willReturn(new Amount(BigDecimal.ONE, Currency.EURO));

        fixture.given(previousEvent)
                .when(command)
                .expectSuccessfulHandlerExecution();

        then(userPaymentService).should(times(2)).pay(anyList(), eq(command.payment()));
    }

    @DisplayName("handle payments should apply events for meal")
    @Test
    void handle_payments_should_apply_events_for_meal() {
        var previousEvent = MealDepositDistributedEventBuilder.appleDepositDistributedEventForJessica().build();
        var command = PayMealCommandBuilder.oneEuroJessicaPaymentCommand().build();

        given(userPaymentService.getBalance(anyList(), eq(command.payment().getDate()), eq(Currency.EURO)))
                .willReturn(new Amount(BigDecimal.ONE, Currency.EURO));

        fixture.given(previousEvent)
                .when(command)
                .expectSuccessfulHandlerExecution();

        then(userPaymentService).should(times(2)).pay(anyList(), eq(command.payment()));
    }
}