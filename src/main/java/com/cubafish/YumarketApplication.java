package com.cubafish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class YumarketApplication {

    static  {
        ApiContextInitializer.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(YumarketApplication.class, args);
    }
}
