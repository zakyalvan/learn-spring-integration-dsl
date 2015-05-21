package com.innovez.learn.second;

import com.innovez.learn.second.xml.ComplexEchoGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Test class for echo gateway configured using xml. Integration using jms,
 * please look complex-source.xml and complex-target.xml spring context config files.
 *
 * @author zakyalvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ComplexFlowXmlConfigurationTest.TestConfiguration.class)
public class ComplexFlowXmlConfigurationTest {
    private static final String DEFAULT_MESSAGE = "This is message payload from spring integration";

    @Autowired
    private ComplexEchoGateway echoGateway;

    @Test
    public void testEchoStringText() {
        String enhanced = echoGateway.echo(DEFAULT_MESSAGE);
        assertThat(enhanced, is(equalTo(DEFAULT_MESSAGE.toUpperCase())));
    }

    /**
     * Testing configuration, loading xml context config and enable spring boot's
     * auto configuration options.
     */
    @Configuration
    @EnableAutoConfiguration
    @ImportResource("classpath*:complex-*.xml")
    static class TestConfiguration {
    }
}
