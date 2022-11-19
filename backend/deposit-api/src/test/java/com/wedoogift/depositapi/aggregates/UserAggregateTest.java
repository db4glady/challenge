package com.wedoogift.depositapi.aggregates;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

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

import com.wedoogift.depositapi.builders.DepositDistributedEventBuilder;
import com.wedoogift.depositapi.builders.DistributeDepositForUserCommandBuilder;
import com.wedoogift.depositapi.builders.MealDepositBuilder;
import com.wedoogift.depositapi.builders.PayItemCommandBuilder;
import com.wedoogift.depositapi.domain.commands.DistributeDepositForUserCommand;
import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;
import com.wedoogift.depositapi.services.ExpirationDateService;
import com.wedoogift.depositapi.services.UserPaymentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * tests de {@link UserAggregate}
 */
@ExtendWith(MockitoExtension.class)
class UserAggregateTest {
    @Mock
    ExpirationDateService expirationDateService;

    @Mock
    UserPaymentService userPaymentService;

    UserAggregate userAggregate;

    FixtureConfiguration<UserAggregate> fixture;

    static Stream<Arguments> notValidDepositCommands() {
        return Stream.of(Arguments.of(
                DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand().withUserId(null).build(),
                DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand().withUserId("").build(),
                DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand().withDeposit(null).build(),
                DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand()
                        .withDeposit(MealDepositBuilder.appleDepositForJessica().withCreationDate(null).build()).build(),
                DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand()
                        .withDeposit(MealDepositBuilder.appleDepositForJessica().withAmount(null).build()).build()));
    }

    @BeforeEach
    void setup() {
        userAggregate = new UserAggregate();
        fixture = new AggregateTestFixture<>(UserAggregate.class)
                .registerInjectableResource(expirationDateService)
                .registerInjectableResource(userPaymentService);
    }

    @ParameterizedTest
    @MethodSource({ "notValidDepositCommands" })
    @DisplayName("handle deposits should throw an IllegalArgumentException input data is not valid")
    void handle_deposits_should_throw_exception_when_user_id_is_null(DistributeDepositForUserCommand command) {
        assertThrows(IllegalArgumentException.class, () -> userAggregate.handle(command, expirationDateService));
    }

    @Test
    @DisplayName("handle deposits should throw an IllegalArgumentException expiration date is not retrieved")
    void handle_deposits_should_throw_exception_when_expiration_date_null() {
        // GIVEN
        var command = DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand().build();

        given(expirationDateService.get(command.deposit())).willReturn(Optional.empty());

        // WHEN THEN
        assertThrows(IllegalArgumentException.class, () -> userAggregate.handle(command, expirationDateService));
    }

    @Test
    @DisplayName("handle deposits should apply events")
    void handle_deposits_should_apply_events() {
        var command = DistributeDepositForUserCommandBuilder.appleDepositForJessicaCommand().build();
        var expirationDate = LocalDate.of(2021, 2, 28);
        given(expirationDateService.get(command.deposit())).willReturn(Optional.of(expirationDate));

        fixture.given()
                .when(command)
                .expectSuccessfulHandlerExecution();

        assertThat(command.deposit().getExpirationDate()).isEqualTo(expirationDate);
    }

    // TODO tests for assertions

    @DisplayName("handle payments should apply events")
    void handle_payments_should_apply_events() {
        var previousEvent = DepositDistributedEventBuilder.appleDepositDistributedEventForJessica().build();
        var command = PayItemCommandBuilder.oneEuroJessicaPaymentCommand().build();

        var expirationDate = LocalDate.of(2021, 2, 28);
        given(expirationDateService.get(previousEvent.deposit())).willReturn(Optional.of(expirationDate));
        given(userPaymentService.getBalance(anyList(), eq(command.payment().getDate()), eq(Currency.EURO)))
                .willReturn(new Amount(BigDecimal.ONE, Currency.EURO));

        fixture.given(previousEvent)
                .when(command)
                .expectSuccessfulHandlerExecution();

        then(userPaymentService).should().pay(anyList(), command.payment());
    }
}