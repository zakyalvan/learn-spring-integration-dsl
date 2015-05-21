#Learn Spring Integration DSL

This project is spring boot based project, intended for learn about configuring spring integration components using new java dsl instead of using 'traditional' xml namespace based config. Another intention is to compare between java dsl configuration approach to 'traditional' xml namespace based. Please note, java dsl configuration enabled by [spring-integration-java-dsl](https://github.com/spring-projects/spring-integration-java-dsl/wiki/Spring-Integration-Java-DSL-Reference) project, so we must add [that project artifact](http://mvnrepository.com/artifact/org.springframework.integration/spring-integration-java-dsl) into project classpath. Following are full ```pom.xml``` file content.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.2.3.RELEASE</version>
    </parent>
    <groupId>com.innovez.learn</groupId>
    <artifactId>learn-spring-integration-dsl</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <java.version>1.8</java.version>
        <start-class>com.innovez.learn.IntegrationApplication</start-class>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.integration</groupId>
                <artifactId>spring-integration-bom</artifactId>
                <version>4.1.3.RELEASE</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-integration</artifactId>
            <!-- Exclude not required libraries, we only need core and jms module -->
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.integration</groupId>
                    <artifactId>spring-integration-file</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework.integration</groupId>
                    <artifactId>spring-integration-stream</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.integration</groupId>
            <artifactId>spring-integration-jms</artifactId>
        </dependency>

        <!-- Library for spring integration java DSL configuration -->
        <dependency>
            <groupId>org.springframework.integration</groupId>
            <artifactId>spring-integration-java-dsl</artifactId>
            <version>1.0.1.RELEASE</version>
        </dependency>

        <!-- ActiveMQ client library, required for establishing jms connection and do jms operation -->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-client</artifactId>
        </dependency>

        <!-- ActiveMQ broker libraries, required if we need to use embedded in memory broker -->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-broker</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-jms-pool</artifactId>
        </dependency>

        <!-- Utility library used in example, not required by spring integration -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>18.0</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

For each learning sample, xml and equivalent java dsl configuration approach are provided. Please look each sample packages inside main ```com.innovez.learn``` package for source; and test ```com.innovez.learn``` package for unit test type. Spring context config files placed inside resource directory, including spring boot's default ```application.properties``` file. Samples in this project are all use very simple scenario, overkill to use spring integration features in real world :D. 

For, all samples using jms, please change ```spring.activemq.in-memory``` config value in ```application.properties``` to adapt your environment. True value means using vm connector and for any reasons you don't want to install dedicated one; or false means using dedicated ActiveMQ jms broker. This is, spring boot specific config item, so please refer spring boot documentations for more details.

### First Example
This sample demonstrate very basic spring integration features. We send message to channel, abstracted using gateway with contract interface ```EchoGateway``` containing one method with signature ```String echo(String content)```. Downstream in processing chain, a transformer configured to do upper case operation to our string type payload, and then return back (via another implicit reply channel) and become return value of gateway. Following are xml configuration for that scenario

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns="http://www.springframework.org/schema/integration"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:beans="http://www.springframework.org/schema/beans"
             xsi:schemaLocation="http://www.springframework.org/schema/beans
                  http://www.springframework.org/schema/beans/spring-beans.xsd
                  http://www.springframework.org/schema/integration
                  http://www.springframework.org/schema/integration/spring-integration.xsd">

    <gateway id="echoGateway" service-interface="com.innovez.learn.first.xml.EchoGateway">
        <method name="echo" request-channel="requestChannel" reply-channel="replyChannel" />
    </gateway>
    <channel id="requestChannel" />
    <transformer id="messageEnhancer"
                 input-channel="requestChannel"
                 output-channel="replyChannel"
                 expression="payload.toUpperCase()" />
    <channel id="replyChannel" />
</beans:beans>
```

We use following test case

```java
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
```

Equivalent java dsl configuration are following

```java
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
```

Following lines are test case for that java dsl configuration.

```java
package com.innovez.learn.first;

import com.innovez.learn.first.java.SimpleFlowConfiguration;
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
public class SimpleFlowJavaDslConfigurationTest {
    @Autowired
    public SimpleFlowConfiguration.EchoGateway echoGateway;

    @Test
    public void testEchoGateway() {
        String result = echoGateway.echo("Muhammad Zaky Alvan");
        MatcherAssert.assertThat(result, IsEqual.equalTo("MUHAMMAD ZAKY ALVAN"));
    }
}
```

### Second Example
