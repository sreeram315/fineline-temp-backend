package com.fineline;

import com.fineline.repo.StudentRepo;
import com.fineline.utils.HazelcastInitializer;
import com.fineline.utils.PopulateData;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot application starter
 * @author Sreeram Maram
 */
@SpringBootApplication
public class FinelineApplication extends SpringBootServletInitializer {

    @Value("${hazelcast.enabled}")
    private boolean hazelcastEnabled;

    public static void main(String[] args) {
        SpringApplication.run(FinelineApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup(StudentRepo studentRepo) {
        return args -> PopulateData.init(studentRepo);
    }

    @Bean
    HazelcastInstance hazelcastInstance() {
        return hazelcastEnabled ? HazelcastInitializer.getHazelcastInstance(): null;
    }

}
