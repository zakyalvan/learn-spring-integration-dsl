package com.innovez.learn.second;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;

import com.innovez.learn.second.java.ComplexJavaFlowConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Simple test case for echo gateway configured using {@link ComplexJavaFlowConfiguration}.
 *
 * @author zakyalvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComplexJavaFlowConfiguration.class)
public class ComplexFlowJavaDslConfigurationTest {
    private static final String DEFAULT_MESSAGE = "This is another message payload from spring integration";

    @Autowired
    private ComplexJavaFlowConfiguration.EchoGateway echoGateway;

    @Test
    public void testEchoGateway() {
        String enhanced = echoGateway.echo(DEFAULT_MESSAGE);
        assertThat(enhanced, is(equalTo(DEFAULT_MESSAGE.toUpperCase())));
    }
}
