package com.dime.ls.subscriberevo.model;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EventReactor {

    private static final Logger LOG = LoggerFactory.getLogger(EventReactor.class);

    @EventListener
    @Async("taskExecutor")
    public void onMessage(Event event) throws InterruptedException {
        Thread.sleep(2000);
        LOG.info("Received: {} --> {}", event.getEventType(), event);
    }
}