package com.wedoogift.depositapi.services;

import java.time.LocalDate;
import java.util.Optional;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;
import com.wedoogift.depositapi.domain.entities.MealDeposit;

/**
 * service computing the expiration date of {@link AbstractDeposit}
 */
public interface ExpirationDateVisitor {

    /**
     * get the expiration date of the meal deposit
     *
     * @param deposit the deposit
     */
    void visit(MealDeposit deposit);

    /**
     * get the expiration date of the gift deposit
     *
     * @param deposit the deposit
     */
    void visit(GiftDeposit deposit);
}
