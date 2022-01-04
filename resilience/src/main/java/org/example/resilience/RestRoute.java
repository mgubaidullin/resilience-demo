package org.example.resilience;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .apiContextPath("api-doc")
                .bindingMode(RestBindingMode.json);

        rest("/")
                .post("/event")
                .consumes("application/json")
                .produces("application/json")
                .type(ResilienceDto.class)
                .outType(ResilienceDto.class)
                .to(direct(ResilienceRoute.URI).getUri());
    }
}