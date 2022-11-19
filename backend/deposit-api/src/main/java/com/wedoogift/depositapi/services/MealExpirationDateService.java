package com.wedoogift.depositapi.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wedoogift.depositapi.domain.entities.MealDeposit;

/**
 * compute the expiration date for {@link MealDeposit}
 */
@Service
class MealExpirationDateService {

    /**
     * get the expiration date of the deposit
     *
     * @param deposit the deposit
     * @return the expiration date
     */
    public Optional<LocalDate> get(MealDeposit deposit) {
        return Optional.ofNullable(deposit)
                .map(MealDeposit::getCreationDate)
                // we compute the expiration date with regard of leap years
                .map(creationDate -> creationDate.plusYears(1)
                        .withMonth(3)
                        .withDayOfMonth(1)
                        .minusDays(1));
    }
}
