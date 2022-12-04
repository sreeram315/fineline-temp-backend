package com.fineline.services.impl;

import com.fineline.services.AmazonSqsService;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;


@ConditionalOnProperty(value="cloud.aws.enabled")
@RestController
public class AmazonSqsServiceImpl implements AmazonSqsService {
    private static final Log LOG = LogFactory.getLog(AmazonSqsServiceImpl.class);

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.queue.end-point}")
    private String endPoint;

    @GetMapping("/send")
    public String send(String message){
        LOG.info(String.format("Sending message: %s", message));
        queueMessagingTemplate.send(endPoint, MessageBuilder.withPayload(message).build());
        return "sent";
    }

    @SqsListener("${cloud.aws.queue.name}")
    public void loadMessagesFromQueue(String message) {
        LOG.info(String.format("Received message: %s", message));
    }
}
