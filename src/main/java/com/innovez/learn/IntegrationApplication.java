package com.innovez.learn;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author zakyalvan
 */
@SpringBootApplication
public class IntegrationApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(IntegrationApplication.class)
                .web(true)
                .build()
                .run(args);
    }
}
