package org.example.resilience;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaRoute extends EndpointRouteBuilder {

    final static String URI = "kafka";

    @Override
    public void configure() throws Exception {

        from(direct(URI)).routeId(URI)
                .marshal().json()
                .to(kafka("events"));
    }

}