package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.DistributeDepositForUserCommand;
import com.wedoogift.depositapi.domain.entities.AbstractDeposit;

/**
 * builder for {@link DistributeDepositForUserCommand}
 */
public class DistributeDepositForUserCommandBuilder {

    private String userId;
    private AbstractDeposit deposit;

    /**
     * private constructor
     */
    private DistributeDepositForUserCommandBuilder() {
    }

    public static DistributeDepositForUserCommandBuilder one() {
        return new DistributeDepositForUserCommandBuilder();
    }

    public static DistributeDepositForUserCommandBuilder teslaDepositForJohnCommand() {
        return one()
                .withDeposit(GiftDepositBuilder.teslaDepositForJohn().build())
                .withUserId("John");
    }

    public static DistributeDepositForUserCommandBuilder appleDepositForJessicaCommand() {
        return one()
                .withDeposit(MealDepositBuilder.appleDepositForJessica().build())
                .withUserId("Jessica");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public DistributeDepositForUserCommand build() {
        return new DistributeDepositForUserCommand(deposit, userId);
    }

    public DistributeDepositForUserCommandBuilder withDeposit(AbstractDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public DistributeDepositForUserCommandBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
