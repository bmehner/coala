package org.openehealth.coala.mocks.xds;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;

public class XdsRouteBuilder extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("xds-iti41:mockrepo?audit=false")
			.process(XdsCamelValidators.iti41RequestValidator())
			.process(new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					ProvideAndRegisterDocumentSet provideAndRegisterDocumentSet = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class);
					List<Document> documents = provideAndRegisterDocumentSet.getDocuments();
					
					Response response = new Response(Status.SUCCESS);
					Exchanges.resultMessage(exchange).setBody(response);
				}
			})
			.process(XdsCamelValidators.iti41ResponseValidator());

	}

}
