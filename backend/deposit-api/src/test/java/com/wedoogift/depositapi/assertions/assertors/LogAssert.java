package com.wedoogift.depositapi.assertions.assertors;

import java.time.Duration;
import java.util.function.Predicate;

import org.assertj.core.api.AbstractObjectAssert;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import static org.awaitility.Awaitility.waitAtMost;

/**
 * Log assertions {@link ListAppender<ILoggingEvent>}
 */
public class LogAssert extends AbstractObjectAssert<LogAssert, ListAppender<ILoggingEvent>> {
    public LogAssert(ListAppender<ILoggingEvent> appender) {
        super(appender, LogAssert.class);
    }

    public void hasLog(Level level, String content) {
        isNotNull();
        if (actual.list.stream().noneMatch(withLog(level, content))) {
            failWithMessage("Log with level %s and content %s not found", level, content);
        }
    }

    public void waitUntilLogFound(Level level, String content) {
        isNotNull();
        waitAtMost(Duration.ofSeconds(10)).until(() -> actual.list.stream().anyMatch(withLog(level, content)));
    }

    private Predicate<ILoggingEvent> withLog(Level level, String content) {
        return event -> level.equals(event.getLevel()) && event.getFormattedMessage().contains(content);
    }

}
