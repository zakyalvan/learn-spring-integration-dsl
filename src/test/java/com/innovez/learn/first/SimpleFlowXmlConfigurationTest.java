package com.innovez.learn.first;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;

import com.innovez.learn.first.xml.EchoGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zakyalvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleFlowXmlConfigurationTest.TestConfiguration.class)
@IntegrationTest
public class SimpleFlowXmlConfigurationTest {
    private static final String MESSAGE_PAYLOAD = "This spring integration message payload";

    @Autowired
    private EchoGateway echoGateway;

    @Test
    public void echoGatewayTest() {
        String enhanced = echoGateway.echo(MESSAGE_PAYLOAD);
        assertThat(enhanced, is(equalTo(MESSAGE_PAYLOAD.toUpperCase())));
    }

    @EnableAutoConfiguration
    @ImportResource("classpath:simple-context.xml")
    public static class TestConfiguration {

    }
}
