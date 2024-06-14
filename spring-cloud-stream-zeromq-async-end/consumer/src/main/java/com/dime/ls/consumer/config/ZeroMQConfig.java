package com.dime.ls.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

@Configuration
public class ZeroMQConfig {

    @Bean(destroyMethod = "close")
    public ZContext zmqContext() {
        return new ZContext();
    }

    @Bean
    public ZMQ.Socket producerSocket(ZContext zmqContext) {
        ZMQ.Socket socket = zmqContext.createSocket(SocketType.PULL);
        socket.bind("tcp://localhost:5555");
        return socket;
    }
}