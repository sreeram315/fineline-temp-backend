package com.fineline.services.impl;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.fineline.services.AmazonSnsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Map.entry;

@ConditionalOnProperty(value="cloud.aws.enabled")
@RestController
public class AmazonSnsServiceImpl implements AmazonSnsService {
    private static final Log LOG = LogFactory.getLog(AmazonSnsServiceImpl.class);

    @Value("${cloud.aws.sns.topic.arn}")
    private String topicArm;

    @Autowired
    AmazonSNSClient amazonSNSClient;

    @PostMapping("/sns/add-subscription")
    public Object addEmailSubscriptionToSNSTopic(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArm, "email", email);
        amazonSNSClient.subscribe(subscribeRequest);
        return Map.ofEntries(
                entry("message", String.format("Subscription request is pending. To confirm the subscription " +
                        "please check your email :%s", email))
        );
    }

    @PostMapping("/sns/send-notification")
    public void publishMessageToSNSTopic(@RequestBody Map<String, Object> payload) {
        LOG.info(String.format("Publishing and message: %s", payload.toString()));
        PublishRequest publishRequest = new PublishRequest(
                topicArm,
                (String) payload.get("body"),
                (String) payload.get("subject"));
        amazonSNSClient.publish(publishRequest);
    }

}