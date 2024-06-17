package com.dime.ls.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

@Configuration
public class ZmqSocketConfig {

    @Bean(destroyMethod = "close")
    public ZMQ.Socket zmqConsumer(){

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket consumer = context.socket(SocketType.ROUTER);
        consumer.bind("tcp://*:5555");

        return consumer;

    }

}
