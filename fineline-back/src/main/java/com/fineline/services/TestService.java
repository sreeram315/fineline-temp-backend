package com.fineline.services;

import org.springframework.web.bind.annotation.GetMapping;

public interface TestService {
    @GetMapping
    String sayHello();
}
