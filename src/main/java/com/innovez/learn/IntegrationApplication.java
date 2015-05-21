package com.innovez.learn;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

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
