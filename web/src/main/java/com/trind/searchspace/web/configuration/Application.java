package com.trind.searchspace.web.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Joachim on 2015-02-05.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.trind.searchspace.web")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
