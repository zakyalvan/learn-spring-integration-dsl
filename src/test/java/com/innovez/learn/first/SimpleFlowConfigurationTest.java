package com.innovez.learn.first;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SimpleFlowConfiguration.class})
@IntegrationTest
public class SimpleFlowConfigurationTest {
    @Autowired
    public SimpleFlowConfiguration.EchoGateway echoGateway;

    @Test
    public void testEchoGateway() {
        String result = echoGateway.echo("Muhammad Zaky Alvan");
        MatcherAssert.assertThat(result, IsEqual.equalTo("MUHAMMAD ZAKY ALVAN"));
    }
}
