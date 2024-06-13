package com.dime.ls.subscriber.source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ZMQ;

@Slf4j
@Configuration
public class EventSubscriber {
    @Bean
    public CommandLineRunner subscribe() {
        return args -> {
            try (ZMQ.Context context = ZMQ.context(1); ZMQ.Socket subscriber = context.socket(ZMQ.SUB)) {
                subscriber.connect("tcp://publisher:5556");
                subscriber.subscribe("".getBytes());
                while (!Thread.currentThread().isInterrupted()) {
                    String message = subscriber.recvStr(0).trim();
                    log.info("Received: {}", message);
                }
            }
        };
    }
}
