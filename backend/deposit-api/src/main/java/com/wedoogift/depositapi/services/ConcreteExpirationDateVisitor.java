package com.wedoogift.depositapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;
import com.wedoogift.depositapi.domain.entities.GiftDeposit;
import com.wedoogift.depositapi.domain.entities.MealDeposit;

/**
 * service computing the expiration date of {@link AbstractDeposit}
 */
@Service
public class ConcreteExpirationDateVisitor implements ExpirationDateVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcreteExpirationDateVisitor.class);

    private final GiftExpirationDateService giftExpirationDateService;
    private final MealExpirationDateService mealExpirationDateService;

    public ConcreteExpirationDateVisitor(GiftExpirationDateService giftExpirationDateService,
            MealExpirationDateService mealExpirationDateService) {
        this.giftExpirationDateService = giftExpirationDateService;
        this.mealExpirationDateService = mealExpirationDateService;
    }

    @Override
    public void visit(MealDeposit deposit) {
        LOGGER.info("compute expiration date for meal deposit");
        mealExpirationDateService.get(deposit)
                .ifPresent(deposit::setExpirationDate);
    }

    @Override
    public void visit(GiftDeposit deposit) {
        LOGGER.info("compute expiration date for gift deposit");
        giftExpirationDateService.get(deposit)
                .ifPresent(deposit::setExpirationDate);
    }
}
