package com.dime.ls.subscriber.handler;

import com.dime.ls.subscriber.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.zeromq.ZMQ;

@Slf4j
@Configuration
public class EventSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner subscribe() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket subscriber = context.socket(ZMQ.SUB)) {
                subscriber.connect("tcp://publisher:5556");
                subscriber.subscribe("".getBytes());
                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] byteMessage = subscriber.recv(0);
                    Event event = objectMapper.readValue(byteMessage, Event.class);
                    log.info("Received: {}", event);
                }
            }
        };
    }
}
