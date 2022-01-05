package org.example.resilience;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptionRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .apiContextPath("api-doc")
                .bindingMode(RestBindingMode.json);

        rest("/")
                .post("/encrypt")
                .consumes("application/json")
                .produces("application/json")
                .type(ResilienceDto.class)
                .outType(ResilienceDto.class)
                .to("direct:encrypt");

        from(direct("encrypt"))
                .log("${body}")
                .process(exchange ->{
                    ResilienceDto in = exchange.getIn().getBody(ResilienceDto.class);
                    ResilienceDto out = new ResilienceDto(in.getClientId(), in.getAmount(), "[encrypted]");
                    exchange.getIn().setBody(out);
                });

    }

}