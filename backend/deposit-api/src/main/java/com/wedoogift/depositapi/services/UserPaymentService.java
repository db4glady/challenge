package com.wedoogift.depositapi.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wedoogift.depositapi.domain.entities.AbstractDeposit;
import com.wedoogift.depositapi.domain.entities.AbstractPayment;
import com.wedoogift.depositapi.domain.entities.Amount;
import com.wedoogift.depositapi.domain.entities.Currency;

/**
 * compute the user balance
 */
@Service
public class UserPaymentService {

    /**
     * comparator to sort the deposit by expiration date
     */
    private static final Comparator<AbstractDeposit> EXPIRATION_DATE_COMPARATOR = (d1, d2) -> {
        var result = 0;
        if (d1.getExpirationDate() == null && d2.getExpirationDate() != null) {
            return 1;
        } else if (d1.getExpirationDate() != null && d2.getExpirationDate() == null) {
            return -1;
        } else if (d1.getExpirationDate() != null && d2.getExpirationDate() != null) {
            result = d1.getExpirationDate().compareTo(d2.getExpirationDate());
        }
        return result;
    };

    /**
     * add the payment to the deposits
     * not thread safe
     *
     * @param deposits the deposits list
     * @param payment  the payment
     */
    public void pay(List<? extends AbstractDeposit> deposits, AbstractPayment payment) {
        var filteredAndSortedList = filterAndSortDeposits(deposits, payment.getDate(), payment.getAmount().currency());
        var restToPay = payment.getAmount().value();

        var iterator = filteredAndSortedList.iterator();
        while (restToPay.compareTo(BigDecimal.ZERO) > 0 && iterator.hasNext()) {
            var currentDeposit = iterator.next();
            var toUse = restToPay.min(currentDeposit.getBalance().value());
            restToPay = restToPay.subtract(toUse);
            currentDeposit.setUsed(currentDeposit.getUsed().plus(toUse));
        }
        if (restToPay.compareTo(BigDecimal.ZERO) > 0) {
            // or add invoice on user account ?
            throw new IllegalStateException("insufficient balance");
        }
    }

    /**
     * compute the balance for the given currency and date
     *
     * @param deposits the deposit list
     * @param date     the date of calculated balance
     * @param currency the currency
     * @return the balance
     */
    public Amount getBalance(List<? extends AbstractDeposit> deposits, LocalDate date, Currency currency) {
        var filteredAndSortedList = filterAndSortDeposits(deposits, date, currency);
        return filteredAndSortedList.stream()
                .map(deposit -> deposit.getAmount().substract(deposit.getUsed()))
                .reduce(Amount::plus)
                .orElse(new Amount(BigDecimal.ZERO, currency));
    }

    /**
     * sort the deposits by expiration date
     * filter :
     * <ul>
     *    <li>expired deposits</li>
     *    <li>not created deposits</li>
     *    <li>deposits in other currencies</li>
     *    <li>not available deposits</li>
     * </ul>
     *
     * @param deposits the deposit list
     * @param date     the date used to exclude expired or not created deposits
     * @param currency the currency used to filter deposits
     * @return the filtered and sorted deposits
     */
    private List<? extends AbstractDeposit> filterAndSortDeposits(List<? extends AbstractDeposit> deposits, LocalDate date, Currency currency) {
        return deposits.stream()
                .filter(AbstractDeposit::isAvailable)
                // should not be null
                .filter(deposit -> deposit.getAmount().currency().equals(currency))
                .filter(deposit -> deposit.getExpirationDate().isAfter(date) || deposit.getExpirationDate().equals(date))
                // eventually
                .filter(deposit -> deposit.getCreationDate().isBefore(date) || deposit.getCreationDate().equals(date))
                .sorted(EXPIRATION_DATE_COMPARATOR)
                .collect(Collectors.toList());
    }
}
