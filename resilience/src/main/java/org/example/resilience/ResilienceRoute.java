package org.example.resilience;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResilienceRoute extends EndpointRouteBuilder {

    final static String URI = "event";

    @Override
    public void configure() throws Exception {

        from(direct(URI)).routeId(URI)
                .to(direct(EncryptionRoute.URI))
                .to(direct(DatabaseRoute.URI_INSERT))
                .to(direct(KafkaRoute.URI))
                .to(direct(DatabaseRoute.URI_REMOVE))
                .unmarshal().json();
    }

}