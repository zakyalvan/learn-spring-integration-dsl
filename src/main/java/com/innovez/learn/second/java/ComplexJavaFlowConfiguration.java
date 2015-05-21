package com.innovez.learn.second.java;

import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.jms.JmsInboundGateway;

import javax.jms.ConnectionFactory;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zakyalvan
 */
@Configuration
@EnableAutoConfiguration
@IntegrationComponentScan
@ComponentScan
public class ComplexJavaFlowConfiguration {
    public static final String REQUEST_CHANNEL_NAME = "requestChannel";

    public static final String INTEGRATION_DESTINATION_NAME = "integrationQueue";

    @Configuration
    public static class SourceConfiguration {
        @Autowired
        @Qualifier("jmsConnectionFactory")
        private ConnectionFactory connectionFactory;

        @Bean
        public IntegrationFlow outboundRequestFlow() {
            return IntegrationFlows.from(REQUEST_CHANNEL_NAME)
                    .split(s -> s.applySequence(true).get().getT2().setDelimiters("\\S"))
                    .handle(Jms.outboundGateway(connectionFactory).requestDestination(INTEGRATION_DESTINATION_NAME),
                            endpointSpec -> endpointSpec.requiresReply(true))
                    .resequence()
                    .aggregate(
                            aggregatorSpec -> aggregatorSpec.outputProcessor(group -> {
                                List<String> payloads = group.getMessages()
                                        .stream()
                                        .map(message -> (String) message.getPayload())
                                        .collect(Collectors.toList());
                                return Joiner.on(" ").join(payloads);
                            }),
                            endpointSpec -> endpointSpec.id("aggregator"))
                    .get();
        }
    }

    @Configuration
    public static class TargetConfiguration {
        @Autowired
        @Qualifier("jmsConnectionFactory")
        private ConnectionFactory connectionFactory;

        @Bean
        public IntegrationFlow inboundEnhanceFlow() {
            JmsInboundGateway inboundGateway = Jms.inboundGateway(connectionFactory)
                    .destination(INTEGRATION_DESTINATION_NAME)
                    .get();

            return IntegrationFlows.from(inboundGateway)
                    .transform("payload.toUpperCase()")
                    .get();
        }
    }

    @MessagingGateway
    public static interface EchoGateway {
        @Gateway(requestChannel = REQUEST_CHANNEL_NAME)
        String echo(String content);
    }
}
