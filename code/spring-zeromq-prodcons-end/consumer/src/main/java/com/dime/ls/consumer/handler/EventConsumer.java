package com.dime.ls.consumer.handler;

import com.dime.ls.consumer.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

@Slf4j
@Component
public class EventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ZMQ.Context context = ZMQ.context(1);

    private final ZMQ.Socket consumer = context.socket(SocketType.ROUTER);

    public EventConsumer() {
        objectMapper.registerModule(new JavaTimeModule());

        consumer.bind("tcp://*:5555");
    }

    @Scheduled(fixedRate = 2500)
    @SneakyThrows(JsonProcessingException.class)
    public void eventConsumption() {

        ZMsg msg = ZMsg.recvMsg(consumer);

        String sock_identity = new String(msg.pop().getData(), ZMQ.CHARSET);
        //log.info("Identity: {}", sock_identity);
        String message = new String(msg.pop().getData(), ZMQ.CHARSET);
        Event event = objectMapper.readValue(message, Event.class);
        log.error("Unable to map JSON to Event");
        log.info("Received: {}", event);
    }

}
