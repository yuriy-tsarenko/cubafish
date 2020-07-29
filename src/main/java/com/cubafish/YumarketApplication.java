package com.cubafish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YumarketApplication {
    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/tomcat");
        SpringApplication.run(YumarketApplication.class, args);
    }
}
