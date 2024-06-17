package com.dime.ls.router.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

@Configuration
public class ZmqSocketConfig {

    @Bean(destroyMethod = "close")
    public ZMQ.Socket zmqRouter(){

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket router = context.socket(SocketType.ROUTER);
        router.bind("tcp://*:5555");

        return router;

    }

}
