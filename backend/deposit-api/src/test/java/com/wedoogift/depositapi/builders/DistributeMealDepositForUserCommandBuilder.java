package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.DistributeMealDepositForUserCommand;
import com.wedoogift.depositapi.domain.entities.MealDeposit;

/**
 * builder for {@link DistributeMealDepositForUserCommand}
 */
public class DistributeMealDepositForUserCommandBuilder {

    private String userId;
    private MealDeposit deposit;

    /**
     * private constructor
     */
    private DistributeMealDepositForUserCommandBuilder() {
    }

    public static DistributeMealDepositForUserCommandBuilder one() {
        return new DistributeMealDepositForUserCommandBuilder();
    }

    public static DistributeMealDepositForUserCommandBuilder appleDepositForJessicaCommand() {
        return one()
                .withDeposit(MealDepositBuilder.appleDepositForJessica().build())
                .withUserId("Jessica");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public DistributeMealDepositForUserCommand build() {
        return new DistributeMealDepositForUserCommand(deposit, userId);
    }

    public DistributeMealDepositForUserCommandBuilder withDeposit(MealDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public DistributeMealDepositForUserCommandBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
