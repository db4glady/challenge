package com.wedoogift.depositapi.services;

import java.time.LocalDate;
import java.util.Optional;

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
public class ExpirationDateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpirationDateService.class);


    private final GiftExpirationDateService giftExpirationDateService;
    private final MealExpirationDateService mealExpirationDateService;

    public ExpirationDateService(GiftExpirationDateService giftExpirationDateService,
            MealExpirationDateService mealExpirationDateService) {
        this.giftExpirationDateService = giftExpirationDateService;
        this.mealExpirationDateService = mealExpirationDateService;
    }

    /**
     * get the expiration date of the deposit
     *
     * @param deposit the deposit
     * @return the expiration date
     */
    public Optional<LocalDate> get(AbstractDeposit deposit) {
        if (deposit instanceof GiftDeposit giftDeposit) {
            LOGGER.info("compute expiration date for gift deposit");
            return giftExpirationDateService.get(giftDeposit);
        } else if (deposit instanceof MealDeposit mealDeposit) {
            LOGGER.info("compute expiration date for meal deposit");
            return mealExpirationDateService.get(mealDeposit);
        }
        return Optional.empty();
    }
}
