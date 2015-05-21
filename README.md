#Learn Spring Integration DSL

This project is spring boot based project, intended for learn about configuring spring integration components using new java dsl instead of using 'traditional' xml namespace based config. Please note, java dsl configuration enabled by [spring-integration-java-dsl](https://github.com/spring-projects/spring-integration-java-dsl/wiki/Spring-Integration-Java-DSL-Reference) project, so we must add [that project artifact](http://mvnrepository.com/artifact/org.springframework.integration/spring-integration-java-dsl) into project classpath.

Another intention is to compare between java dsl configuration approach with 'traditional' xml namespace based approach. For each sample, xml and java dsl configuration approach are used.

Please look each sample packages inside main ```com.innovez.learn``` package for source; and test ```com.innovez.learn``` package for test type. Spring context config files placed inside resource directory, including ```application.properties``` file. Samples in this project are all use very simple scenario, overkill to be used in real world :D. 

For, all samples using jms, please change ```spring.activemq.in-memory``` config value in ```application.properties``` to adapt your environment. True value means using vm connector and for any reasons you don't want to install dedicated one; or false means using dedicated ActiveMQ jms broker. This is, spring boot specific config item, so please refer spring boot documentations for more details.