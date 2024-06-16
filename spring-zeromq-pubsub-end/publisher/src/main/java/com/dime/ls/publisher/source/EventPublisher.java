package com.dime.ls.publisher.source;

import com.dime.ls.publisher.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Slf4j
@Component
public class EventPublisher {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ZMQ.Context context = ZMQ.context(1);

    private final ZMQ.Socket publisher = context.socket(SocketType.PUB);

    public EventPublisher(){

        objectMapper.registerModule(new JavaTimeModule());

        publisher.bind("tcp://*:5555");

    }

    @Scheduled(fixedRate = 1000, initialDelay = 8000)
    public void eventPublish() {

        Event event = Event.builder()
                .data("Hello World!")
                .key(UUID.randomUUID().toString())
                .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                .build();

        String topic = "topic-" + RANDOM.nextInt(0,3);
        log.info("Choosen topic is {}", topic);

        String stringEvent = null;
        try{
            stringEvent = objectMapper.writeValueAsString(event);
        } catch(JsonProcessingException e) {
            log.error("Unable to map event to JSON");
        } finally {
          publisher.sendMore(topic.getBytes(ZMQ.CHARSET));
          publisher.send(stringEvent.getBytes(ZMQ.CHARSET));
          log.info("Event published: {}", stringEvent);
        }

    }
}
