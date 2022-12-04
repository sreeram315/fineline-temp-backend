package com.fineline.services;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

public interface AmazonSnsService {

    @PostMapping
    Object addEmailSubscriptionToSNSTopic(Map<String, Object> payload) ;

    @PostMapping
    void publishMessageToSNSTopic(Map<String, Object> payload) ;
}
