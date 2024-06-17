package com.dime.ls.consumer.handler;

import com.dime.ls.consumer.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

@Slf4j
@Component
public class EventConsumer {

    private ObjectMapper objectMapper;

    private ZMQ.Socket consumer;

    @Autowired
    public EventConsumer(ZMQ.Socket consumer, ObjectMapper objectMapper) {

        this.consumer = consumer;

        this.objectMapper = objectMapper;

    }

    @Scheduled(fixedRate = 2500)
    @SneakyThrows(JsonProcessingException.class)
    public void eventConsumption() {

        ZMsg msg = ZMsg.recvMsg(consumer);

        String sock_identity = new String(msg.pop().getData(), ZMQ.CHARSET);
        //log.info("Identity: {}", sock_identity);
        String message = new String(msg.pop().getData(), ZMQ.CHARSET);
        Event event = objectMapper.readValue(message, Event.class);
        log.info("Received: {}", event);
    }

}
