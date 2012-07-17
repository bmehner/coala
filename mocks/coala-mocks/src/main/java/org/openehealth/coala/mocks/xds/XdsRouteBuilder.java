package org.openehealth.coala.mocks.xds;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;

public class XdsRouteBuilder extends SpringRouteBuilder {

	@Resource(name="registryRepositoryService")
	private RegistryRepositoryService registryRepositoryService;
	
	@Override
	public void configure() throws Exception {

		from("xds-iti18:registryStoredQuery?audit=false")
			.process(new Processor(){

				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println(exchange);
				}
				
			})
			.process(XdsCamelValidators.iti18RequestValidator())
			.process(new Processor(){

				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println(exchange);
				}
				
			})
			.beanRef("registryRepositoryService", "registryStoredQuery")
			.process(new Processor(){

				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println(exchange);
				}
				
			})
//			.process(new Processor() {
//				@Override
//				public void process(Exchange exchange) throws Exception {
//					FindDocumentsQuery query  = exchange.getIn().getBody(FindDocumentsQuery.class);
//					QueryResponse registryStoredQuery = registryRepositoryService.registryStoredQuery(query);
//					registryStoredQuery.setStatus(Status.SUCCESS);
//					Exchanges.resultMessage(exchange).setBody(registryStoredQuery);
//				}
//			})
			.process(XdsCamelValidators.iti18ResponseValidator());
		
		from("xds-iti41:provideAndRegister?audit=false")
			.process(XdsCamelValidators.iti41RequestValidator())
			.process(new Processor(){

				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println(exchange);
				}
				
			})
			.beanRef("registryRepositoryService", "provideAndRegister")
			.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
//					ProvideAndRegisterDocumentSet provideAndRegisterDocumentSet = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class);
//					registryRepositoryService.provideAndRegister(provideAndRegisterDocumentSet);
					Response response = new Response(Status.SUCCESS);
					Exchanges.resultMessage(exchange).setBody(response);
				}
			})
			.process(XdsCamelValidators.iti41ResponseValidator());
		
		from("xds-iti43:retrieveDocuments?audit=false")
			.process(XdsCamelValidators.iti43RequestValidator())
			.beanRef("registryRepositoryService", "retrieveDocuments")
//			.process(new Processor() {
//				@Override
//				public void process(Exchange exchange) throws Exception {
//					RetrieveDocumentSet retrieveDocumentSet = exchange.getIn().getBody(RetrieveDocumentSet.class);
//					RetrievedDocumentSet retrieveDocuments = registryRepositoryService.retrieveDocuments(retrieveDocumentSet);
//					retrieveDocuments.setStatus(Status.SUCCESS);
//					Exchanges.resultMessage(exchange).setBody(retrieveDocuments);
//				}
//			})
			.process(XdsCamelValidators.iti43ResponseValidator());

	}

}
