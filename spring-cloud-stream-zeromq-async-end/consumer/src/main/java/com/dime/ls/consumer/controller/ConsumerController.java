package com.dime.ls.consumer.controller;

import com.dime.ls.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    private final ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/receive")
    public ResponseEntity<String> receiveMessage() {
        String message = consumerService.receiveMessage();
        return ResponseEntity.ok(message);
    }
}