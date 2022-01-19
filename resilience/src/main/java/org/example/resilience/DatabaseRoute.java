package org.example.resilience;

import com.mongodb.client.model.Filters;
import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

import static org.apache.camel.builder.endpoint.dsl.MongoDbEndpointBuilderFactory.MongoDbOperation.*;

@ApplicationScoped
public class DatabaseRoute extends EndpointRouteBuilder {

    final static String URI_INSERT = "mongo-service-call";
    final static String URI_UPDATE = "mongo-service-update";

    final static String CLIENT = "mongoClient";
    final static String DATABASE = "test";
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

        from(direct(URI_UPDATE)).routeId(URI_UPDATE)
                .process(e -> e.getIn().setBody(List.of(
                        Filters.eq("_id", e.getIn().getHeader("CamelMongoOid")),
                        new BsonDocument().append("$set", new BsonDocument("kafka", new BsonString(e.getIn().getHeader("kafka", "done", String.class))))
                )))
                .to(mongodb(CLIENT).database(DATABASE).collection(COLLECTION).operation(update))
                .setBody(simple("${headers.CamelMongoOid}"))
                .to(mongodb(CLIENT).database(DATABASE).collection(COLLECTION).operation(findById));
    }
}