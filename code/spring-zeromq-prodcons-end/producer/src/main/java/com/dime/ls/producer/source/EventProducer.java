package com.dime.ls.producer.source;

import com.dime.ls.producer.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Component
public class EventProducer {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private ObjectMapper objectMapper;

    private ZMQ.Socket producer;

    @Autowired
    public EventProducer(ZMQ.Socket producer, ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;

        this.producer = producer;

    }

    @Scheduled(fixedRate = 1000)
    @SneakyThrows(JsonProcessingException.class)
    public void eventProduction() {
        Event event = Event.builder()
                .data("Hello World!")
                .key(UUID.randomUUID().toString())
                .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                .build();
        String stringEvent = objectMapper.writeValueAsString(event);
        log.info("Sent: {}", stringEvent);
        producer.send(stringEvent);
    }
}