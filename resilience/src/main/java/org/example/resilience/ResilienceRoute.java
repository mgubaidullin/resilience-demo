package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResilienceRoute extends EndpointRouteBuilder {

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
                .to("direct:event");

        from(direct("event"))
                .log("${body.getClass()}")
                .to(direct(EncryptionRoute.URI))
                .log("${body.getClass()}")
//                .marshal().json()
                .to(direct(DatabaseRoute.URI))
                .log("${body}");

    }

}