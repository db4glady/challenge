package com.wedoogift.depositapi.builders;

import com.wedoogift.depositapi.domain.commands.DistributeDepositFromCompanyCommand;
import com.wedoogift.depositapi.domain.entities.AbstractDeposit;

/**
 * builder for {@link DistributeDepositFromCompanyCommand}
 */
public class DistributeDepositForCompanyCommandBuilder {

    private String companyId;
    private AbstractDeposit deposit;

    /**
     * private constructor
     */
    private DistributeDepositForCompanyCommandBuilder() {
    }

    public static DistributeDepositForCompanyCommandBuilder one() {
        return new DistributeDepositForCompanyCommandBuilder();
    }

    public static DistributeDepositForCompanyCommandBuilder teslaDepositForJohnCommand() {
        return one()
                .withDeposit(GiftDepositBuilder.teslaDepositForJohn().build())
                .withCompanyId("Tesla");
    }

    public static DistributeDepositForCompanyCommandBuilder appleDepositForJessicaCommand() {
        return one()
                .withDeposit(MealDepositBuilder.appleDepositForJessica().build())
                .withCompanyId("Apple");
    }

    /**
     * build the command
     *
     * @return the command
     */
    public DistributeDepositFromCompanyCommand build() {
        return new DistributeDepositFromCompanyCommand(deposit, companyId);
    }

    public DistributeDepositForCompanyCommandBuilder withDeposit(AbstractDeposit deposit) {
        this.deposit = deposit;
        return this;
    }

    public DistributeDepositForCompanyCommandBuilder withCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }
}
