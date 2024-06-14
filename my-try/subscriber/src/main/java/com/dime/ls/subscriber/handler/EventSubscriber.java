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
import org.zeromq.ZMsg;

@Slf4j
@Configuration
public class EventSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public CommandLineRunner subscribe() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket subscriber = context.socket(SocketType.ROUTER)) {
                subscriber.bind("tcp://*:5555");

                objectMapper.registerModule(new JavaTimeModule());
                while (!Thread.currentThread().isInterrupted()) {
                    ZMsg msg = ZMsg.recvMsg(subscriber);

                    String sock_identity = new String(msg.pop().getData(), ZMQ.CHARSET);
                    String message = new String(msg.pop().getData(), ZMQ.CHARSET);
                    Event event = objectMapper.readValue(message, Event.class);
                    log.info("Identity: {}", sock_identity);
                    log.info("Received: {}", event);
                    Thread.sleep(2000);
                }
            }
        };
    }
}
