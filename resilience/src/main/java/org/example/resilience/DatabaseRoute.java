package org.example.resilience;

import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;

import static org.apache.camel.builder.endpoint.dsl.MongoDbEndpointBuilderFactory.MongoDbOperation.insert;


@ApplicationScoped
public class DatabaseRoute extends EndpointRouteBuilder {

    final static String URI_INSERT = "mongo-service-call";

    final static String CLIENT = "mongoClient";
    final static String DATABASE = "resilience";
    final static String COLLECTION = "event";
    final static String ENCRYPTED = "encrypted";

    @Override
    public void configure() throws Exception {

        from(direct(URI_INSERT)).routeId(URI_INSERT)
                .onException(Exception.class).maximumRedeliveries(2).handled(true)
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                    .setBody(constant("Mongo service not available"))
                .end()
                .log("Mongo service route")
                .removeHeader(Exchange.HTTP_URI)
                .removeHeader(Exchange.HTTP_PATH)
                .removeHeader(Exchange.HTTP_RESPONSE_CODE)
                .setHeader(ENCRYPTED, simple("${body}"))
                .circuitBreaker().inheritErrorHandler(true).faultToleranceConfiguration().requestVolumeThreshold(10).end()
                    .log("Mongo service call start")
                    .to(mongodb(CLIENT).database(DATABASE).collection(COLLECTION).operation(insert))
                    .setBody(simple("${headers.encrypted}"))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                    .log("Mongo service call end")
                .end();
    }

}