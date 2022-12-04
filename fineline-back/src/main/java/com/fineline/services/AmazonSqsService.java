package com.fineline.services;

import org.springframework.web.bind.annotation.GetMapping;

public interface AmazonSqsService {
    @GetMapping
    String send(String message);

    @SuppressWarnings("unused")
    void loadMessagesFromQueue(String message) ;
}
