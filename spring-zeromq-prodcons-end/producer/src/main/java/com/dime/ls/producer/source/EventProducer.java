package com.dime.ls.producer.source;

import com.dime.ls.producer.model.Event;
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
public class EventProducer {

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ZMQ.Context context = ZMQ.context(1);

    private final ZMQ.Socket producer = context.socket(SocketType.DEALER);

    public EventProducer() {
        objectMapper.registerModule(new JavaTimeModule());

        //Containerized running configuration
        producer.setIdentity(System.getenv("IDENTITY").trim().getBytes(ZMQ.CHARSET));
        String[] consumersNames = System.getenv("CONSUMERS_NAMES").split(",");
        for (String consumer : consumersNames) producer.connect("tcp://" + consumer.trim() + ":5555");

        //Local running configuration
        //producer.setIdentity("PRODUCER-0".getBytes(ZMQ.CHARSET));
        //producer.connect("tcp://localhost:5555");
    }

    @Scheduled(fixedRate = 1000)
    public void EventProduction() {
        Event event = Event.builder()
                            .data("Hello World!")
                            .key(UUID.randomUUID().toString())
                            .eventType(Event.Type.class.getEnumConstants()[RANDOM.nextInt(Event.Type.class.getEnumConstants().length)])
                           .build();
        String stringEvent = null;
        try {
            stringEvent = objectMapper.writeValueAsString(event);
        }catch (JsonProcessingException e){
            log.error("Unable to map event to JSON");
        } finally{
            log.info("Sent: {}", stringEvent);
            producer.send(stringEvent);
        }
    }
}