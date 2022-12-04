package com.fineline.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private static final Log LOG = LogFactory.getLog(TestController.class);

    @GetMapping("/ping")
    String sayHello() {
        LOG.info("Called sayHello");
        return "hello";
    }
}
