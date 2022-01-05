package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.RestEndpointBuilderFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptionRoute extends EndpointRouteBuilder {

    @ConfigProperty(name = "encryption.host")
    String encryptionHost;

    final static String URI = "encrypt-service-call";

    @Override
    public void configure() throws Exception {

        RestEndpointBuilderFactory.RestEndpointProducerBuilder rest = restEndpoint("post:encrypt")
                .host(encryptionHost)
                .inType("org.example.resilience.ResilienceDto")
                .outType("org.example.resilience.ResilienceDto");

        from(direct(URI)).routeId(URI)
                .onException(Exception.class).maximumRedeliveries(2).handled(true)
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                    .setBody(constant("Encryption service not available"))
                .end()
                .log("Encryption service route")
                .removeHeader(Exchange.HTTP_URI)
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .circuitBreaker().inheritErrorHandler(true)
                    .faultToleranceConfiguration().requestVolumeThreshold(10).end()
                    .log("Encryption service call start")
                    .to(rest)
                    .log("Encryption service call end")
                .end();
    }

}