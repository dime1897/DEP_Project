package com.dime.ls.producer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

@Configuration
public class ZmqSocketConfig {

    @Value("${IDENTITY}")
    private String identity;
    
    @Value("${CONSUMERS_NAMES}")
    private String consumersNames;
    
    @Value("${DELIMITER}")
    private String delimiter;
    
    @Bean(destroyMethod = "close")
    public ZMQ.Socket zmqProducer(){

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket producer = context.socket(SocketType.DEALER);
        
        producer.setIdentity(this.identity.trim().getBytes(ZMQ.CHARSET));
        String[] consumersNamesArray = this.consumersNames.split(delimiter);
        for (String consumer : consumersNamesArray) producer.connect("tcp://" + consumer.trim() + ":5555");

        return producer;

    }
}
