package com.dime.ls.publisher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

@Configuration
public class ZmqSocketConfig {

    private final String publisherAddress = "tcp://*:5555";

    @Bean(destroyMethod = "close")
    public ZMQ.Socket zmqPublisher() {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(SocketType.PUB);

        publisher.bind(publisherAddress);

        return publisher;
    }

}
