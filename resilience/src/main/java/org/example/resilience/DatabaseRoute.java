package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseRoute extends EndpointRouteBuilder {

    final static String URI = "database";

    @Override
    public void configure() throws Exception {

        from(direct(URI))
                .to("mongodb:mongoClient?database=resilience&collection=events&operation=insert");
    }

}