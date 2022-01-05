package org.example.resilience;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.MongoDbEndpointBuilderFactory;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class DatabaseRoute extends EndpointRouteBuilder {

    final static String URI_INSERT = "databaseInsert";
    final static String URI_REMOVE = "databaseRemove";

    final static String CLIENT = "mongoClient";
    final static String DATABASE = "resilience";
    final static String COLLECTION = "event";
    final static String ID_FIELD = "_id";
    final static String ENCRYPTED = "encrypted";

    @Override
    public void configure() throws Exception {

//        from(direct(URI_INSERT)).routeId(URI_INSERT)
//                .setHeader(ENCRYPTED, simple("${body}"))
//                .to(mongodb(CLIENT).database(DATABASE).collection(COLLECTION).operation(insert))
//                .setHeader(ID_FIELD, simple("${body.get('_id')}"))
//                .setBody(simple("${headers.encrypted}"));
//
//        from(direct(URI_REMOVE)).routeId(URI_REMOVE)
//                .setHeader(ENCRYPTED, simple("${body}"))
//                .process(e-> e.getIn().setBody(Filters.eq(ID_FIELD, e.getIn().getHeader(ID_FIELD))))
//                .to(mongodb(CLIENT).database(DATABASE).collection(COLLECTION).operation(remove))
//                .setBody(simple("${headers.encrypted}"));
    }

}