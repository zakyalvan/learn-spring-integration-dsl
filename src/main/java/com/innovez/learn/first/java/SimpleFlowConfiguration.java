package com.innovez.learn.first.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

/**
 * @author zakyalvan
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class SimpleFlowConfiguration {
    public static final String REQUEST_CHANNEL = "requestChannel";

    @Bean(name = REQUEST_CHANNEL)
    public DirectChannel requestChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow integrationFlow() {
        /**
         * However we can use channel name as parameter of ```from``` method,
         * spring integration will create that channel implicitly in not already exists
         * in application context.
         */
        return IntegrationFlows.from(requestChannel())
                .transform((String payload) -> payload.toUpperCase())
                .get();
    }

    @MessagingGateway
    public static interface EchoGateway {
        @Gateway(requestChannel = REQUEST_CHANNEL)
        String echo(String content);
    }
}
