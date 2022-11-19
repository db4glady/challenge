package com.wedoogift.depositapi.assertions;

import org.assertj.core.api.Assertions;

import com.wedoogift.depositapi.assertions.assertors.AmountAssert;
import com.wedoogift.depositapi.assertions.assertors.LogAssert;
import com.wedoogift.depositapi.domain.entities.Amount;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class DepositApiAssertions extends Assertions {

    public static LogAssert assertThat(final ListAppender<ILoggingEvent> appender) {
        return new LogAssert(appender);
    }

    public static AmountAssert assertThat(final Amount amount) {
        return new AmountAssert(amount);
    }
}
