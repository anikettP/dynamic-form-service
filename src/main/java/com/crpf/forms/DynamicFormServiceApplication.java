package com.crpf.forms;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DynamicFormServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamicFormServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));
    }
}
