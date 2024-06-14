package com.dime.ls.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;

@Service
public class ProducerService {

    private final ZMQ.Socket producerSocket;

    @Autowired
    public ProducerService(ZMQ.Socket producerSocket) {
        this.producerSocket = producerSocket;
    }

    public void sendMessage(String message) {
        producerSocket.send(message.getBytes(ZMQ.CHARSET), ZMQ.DONTWAIT);
    }
}
