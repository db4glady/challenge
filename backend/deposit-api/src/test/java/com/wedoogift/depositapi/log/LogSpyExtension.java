package com.wedoogift.depositapi.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * extension JUnit permettant de tester les logs avec logback
 */
public class LogSpyExtension implements BeforeEachCallback, AfterEachCallback {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogSpyExtension.class);
    /**
     * stocke la liste des loggers et des appenders de tests correspondants
     */
    private Map<Logger, ListAppender<ILoggingEvent>> loggersAppendersMap;

    @Override
    public void beforeEach(ExtensionContext context) {
        // on initialise la liste des loggers
        loggersAppendersMap = new HashMap<>();
        context.getTestInstances()
                .stream()
                .map(TestInstances::getAllInstances)
                .flatMap(List::stream)
                .forEach(instance -> Stream.of(instance.getClass().getDeclaredFields())
                        // on filtre les champs annotés avec SpyLogAppender
                        .filter(field -> field.isAnnotationPresent(SpyLogAppender.class))
                        .forEach(field -> {
                            try {
                                // on récupère le nom du logger
                                String loggerName = field.getAnnotation(SpyLogAppender.class).logger();
                                // on crée l'appender de test
                                ListAppender<ILoggingEvent> loggingEventListAppender = new ListAppender<>();
                                // on affecte l'appender
                                field.setAccessible(true);
                                field.set(instance, loggingEventListAppender);
                                // on ajoute l'appender au logger
                                Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
                                logger.addAppender(loggingEventListAppender);
                                loggersAppendersMap.put(logger, loggingEventListAppender);
                                // on démarre l'appender
                                loggingEventListAppender.start();
                            } catch (IllegalAccessException e) {
                                LOGGER.error("failed to create test appender", e);
                            }
                        }));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        // on arrête les appenders de test
        loggersAppendersMap.forEach((logger, appender) -> {
            logger.detachAppender(appender);
            appender.stop();
        });
    }
}
