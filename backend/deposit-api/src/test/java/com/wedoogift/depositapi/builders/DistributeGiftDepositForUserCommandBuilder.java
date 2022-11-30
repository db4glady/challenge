package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.DistributeGiftDepositForUserCommand;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;

/**
 * builder for {@link DistributeGiftDepositForUserCommand}
 */
public class DistributeGiftDepositForUserCommandBuilder {

    private String userId;
    private GiftDeposit deposit;

    /**
     * private constructor
     */
    private DistributeGiftDepositForUserCommandBuilder() {
    }

    public static DistributeGiftDepositForUserCommandBuilder one() {
        return new DistributeGiftDepositForUserCommandBuilder();
    }

    public static DistributeGiftDepositForUserCommandBuilder teslaDepositForJohnCommand() {
        return one()
                .withDeposit(GiftDepositBuilder.teslaDepositForJohn().build())
                .withUserId("John");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public DistributeGiftDepositForUserCommand build() {
        return new DistributeGiftDepositForUserCommand(deposit, userId);
    }

    public DistributeGiftDepositForUserCommandBuilder withDeposit(GiftDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public DistributeGiftDepositForUserCommandBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
