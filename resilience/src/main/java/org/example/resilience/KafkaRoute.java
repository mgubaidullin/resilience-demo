package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaRoute extends EndpointRouteBuilder {

    final static String URI = "kafka";

    @Override
    public void configure() throws Exception {

        from(direct(URI)).routeId(URI)
                .onException(Exception.class).maximumRedeliveries(2).handled(true)
                    .setHeader("kafka", constant("not_sent"))
                    .to(direct(DatabaseRoute.URI_UPDATE))
                .end()
                .log("Kafka service route")
                .circuitBreaker().inheritErrorHandler(true).faultToleranceConfiguration().requestVolumeThreshold(10).end()
                    .log("Kafka service call start")
                    .to(kafka("events"))
                    .log("Kafka service call end")
                .end();
    }

}