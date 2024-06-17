package com.dime.ls.subscriber.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

@Configuration
public class ZmqSocketConfig {

    //@Value("${publisher.port}")
    //private int publisherPort;

    private final String publisherAddress = "tcp://publisher:5555"; //subscriber.connect("tcp://localhost:5555");

    @Value("${TOPIC_NAMES}")
    private String topicNamesEnv;

    @Bean(destroyMethod = "close")
    public ZMQ.Socket zmqSubscriber() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);

        subscriber.connect(publisherAddress);
        String[] topics = topicNamesEnv.split(",");
        for (String topic : topics) {
            subscriber.subscribe(topic.trim().getBytes(ZMQ.CHARSET)); //subscriber.subscribe("topic-0".getBytes());
        }

        return subscriber;
    }

}
