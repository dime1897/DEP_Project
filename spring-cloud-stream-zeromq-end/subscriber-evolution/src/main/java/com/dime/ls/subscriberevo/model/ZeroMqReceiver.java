package com.dime.ls.subscriberevo.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class ZeroMqReceiver {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Async("taskExecutor")
    public void receiveMessages() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.PULL);
            socket.connect("tcp://localhost:5555");

            objectMapper.registerModule(new JavaTimeModule());

            while (!Thread.currentThread().isInterrupted()) {
                byte[] byteMessage = socket.recv(0); // Metodo bloccante
                Event event = objectMapper.readValue(byteMessage, Event.class);
                applicationEventPublisher.publishEvent(event);
                System.out.println("Done");
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
