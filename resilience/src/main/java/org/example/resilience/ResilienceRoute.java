package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResilienceRoute extends EndpointRouteBuilder {

    final static String URI_START = "start";
    final static String URI_STORE = "store";

    @Override
    public void configure() throws Exception {

        from(direct(URI_START)).routeId(URI_START)
                .to(direct(EncryptionRoute.URI))
                .log("${headers}")
                .log("${body}")
                .choice()
                    .when(header(Exchange.HTTP_RESPONSE_CODE).contains(200))
                        .to(direct(URI_STORE))
                    .endChoice()
                    .otherwise()
                        .setBody(constant("Encryption service not available (longer than usual)"))
                    .endChoice();

        from(direct(URI_STORE)).routeId(URI_STORE)
                .log("STORE");
//                .to(direct(DatabaseRoute.URI_INSERT))
//                .to(direct(KafkaRoute.URI))
//                .to(direct(DatabaseRoute.URI_REMOVE))
//                .unmarshal().json();
    }

}