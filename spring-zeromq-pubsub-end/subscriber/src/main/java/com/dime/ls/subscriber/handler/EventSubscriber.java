package com.dime.ls.subscriber.handler;

import com.dime.ls.subscriber.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

@Slf4j
@Configuration
public class EventSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner receive() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket subscriber = context.socket(SocketType.SUB)) {
                subscriber.connect("tcp://publisher:5555");

                String[] topicNames = System.getenv("TOPIC_NAMES").split(",");
                for (String topicName : topicNames) subscriber.subscribe(topicName.trim().getBytes(ZMQ.CHARSET)); //SUB topic specified in env variables by docker-compose-file

                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    String topic = subscriber.recvStr();
                    log.info("Received on topic: {}", topic);
                    byte[] message = subscriber.recv();
                    Event event = objectMapper.readValue(message, Event.class);
                    log.info("Received: {}", event);
                    Thread.sleep(2500);
                }
            }
        };
    }
}
