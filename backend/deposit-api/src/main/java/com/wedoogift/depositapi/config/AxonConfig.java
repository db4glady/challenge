package com.wedoogift.depositapi.config;

import javax.annotation.PostConstruct;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.config.Configurer;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public SimpleCommandBus commandBus() {
        return SimpleCommandBus.builder()
                .build();
    }

    @Bean
    public SnapshotTriggerDefinition userSnapshotTrigger(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 10);
    }

    @Bean
    public SnapshotTriggerDefinition companySnapshotTrigger(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 10);
    }
}
