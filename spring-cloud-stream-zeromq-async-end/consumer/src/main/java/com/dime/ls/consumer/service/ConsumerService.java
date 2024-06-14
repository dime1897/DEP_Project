package com.dime.ls.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;

@Service
public class ConsumerService {

    private final ZMQ.Socket consumerSocket;

    @Autowired
    public ConsumerService(ZMQ.Socket consumerSocket) {
        this.consumerSocket = consumerSocket;
    }

    public String receiveMessage() {
        byte[] message = consumerSocket.recv(0);
        return new String(message, ZMQ.CHARSET);
    }
}
