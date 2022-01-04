package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptionRoute extends EndpointRouteBuilder {

    @ConfigProperty(name = "encryption.host")
    String encryptionHost;

    final static String URI = "encrypt";

    @Override
    public void configure() throws Exception {

        from(direct(URI))
                .removeHeader(Exchange.HTTP_URI)
                .removeHeader(Exchange.HTTP_PATH)
                .to(restEndpoint("post:encrypt")
                        .host(encryptionHost)
                        .inType("org.example.resilience.ResilienceDto")
                        .outType("org.example.resilience.ResilienceDto")
                );
    }

}