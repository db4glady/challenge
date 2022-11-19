package com.wedoogift.depositapi.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wedoogift.depositapi.domain.entities.GiftDeposit;

/**
 * compute the expiration date for {@link GiftDeposit}
 */
@Service
class GiftExpirationDateService {

    private static final int VALIDITY_DAYS = 365;

    /**
     * get the expiration date of the deposit
     *
     * @param deposit the deposit
     * @return the expiration date
     */
    public Optional<LocalDate> get(GiftDeposit deposit) {
        return Optional.ofNullable(deposit)
                .map(GiftDeposit::getCreationDate)
                .map(creationDate -> creationDate.plusDays(VALIDITY_DAYS - 1));
    }
}
