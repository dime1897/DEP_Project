package com.dime.ls.subscriber.handler;

import com.dime.ls.subscriber.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.io.IOException;

@Slf4j
@Component
public class EventSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ZMQ.Context context = ZMQ.context(1);

    private final ZMQ.Socket subscriber = context.socket(SocketType.SUB);

    public EventSubscriber() {

        objectMapper.registerModule(new JavaTimeModule());

        //Local running configuration
        //subscriber.connect("tcp://localhost:5555");
        //subscriber.subscribe("topic-0".getBytes());

        //Containerized running configuration
        subscriber.connect("tcp://publisher:5555");
        String[] topicNames = System.getenv("TOPIC_NAMES").split(",");
        for (String topicName : topicNames) subscriber.subscribe(topicName.trim().getBytes(ZMQ.CHARSET));

    }

    @Scheduled(fixedRate = 2500)
    @SneakyThrows(IOException.class)
    public void eventRetrieving(){

        String topic = subscriber.recvStr();
        log.info("Got event from topic: {}", topic);

        byte[] byteEvent = subscriber.recv();
        Event event = objectMapper.readValue(byteEvent, Event.class);
        log.error("Unable to map JSON to event");
        log.info("Got event: {}", event);

    }
}
