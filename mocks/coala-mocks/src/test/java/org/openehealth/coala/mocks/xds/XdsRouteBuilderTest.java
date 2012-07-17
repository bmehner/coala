/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openehealth.coala.mocks.xds;

import static org.junit.Assert.*;
import static org.openehealth.coala.mocks.xds.XdsTestUtils.createProvideAndRegisterDocumentSet;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.coala.mocks.JettyStarter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.springframework.context.ApplicationContext;

public class XdsRouteBuilderTest {

	private static final int PORT = 8766;
	private static final String URI_PROVIDE_AND_REGISTER = "xds-iti41://localhost:" + PORT + "/provideAndRegister?audit=false";
	private static final String URI_QUERY_REGISTRY = "xds-iti18://localhost:" + PORT + "/registryStoredQuery?audit=false";

	
	static ApplicationContext appContext;
	static ProducerTemplate template;
	static CamelContext context;
	
	@BeforeClass
	public static void startServer() {
		appContext = JettyStarter.startJettyServer(PORT, "META-INF/spring/coala-mocks-context.xml");
		template = (ProducerTemplate) appContext.getBean("template");
        context = (CamelContext) appContext.getBean("camelContext"); 
	}
	
	@Test
	public void testProvideAndRegisterIti41() throws Exception {
		Exchange exchange = new DefaultExchange(context);
		
		ProvideAndRegisterDocumentSet request = createProvideAndRegisterDocumentSet();
		exchange.getIn().setBody(request);
		Exchange result = template.send(URI_PROVIDE_AND_REGISTER, exchange);
		Response response = Exchanges.resultMessage(result).getBody(Response.class);
		assertEquals(Status.SUCCESS, response.getStatus());
	}
	
	@Test
	public void testQueryRegistryIti18() throws Exception {
		Exchange exchange = new DefaultExchange(context);
		
		ProvideAndRegisterDocumentSet request = createProvideAndRegisterDocumentSet();
		Identifiable patientId = request.getDocuments().get(0).getDocumentEntry().getPatientId();
		String testId = "42";
		patientId.setId(testId);
		exchange.getIn().setBody(request);
		Exchange result = template.send(URI_PROVIDE_AND_REGISTER, exchange);
		Response response = Exchanges.resultMessage(result).getBody(Response.class);
		assertEquals(Status.SUCCESS, response.getStatus());
		
		Exchange requestExchange = new DefaultExchange(context);

		FindDocumentsQuery query = new FindDocumentsQuery();
		query.setPatientId(patientId);

		// setting status of documents as given by parameter (method caller)
		//query.setStatus(availabilityStati);

		// Prepare request
		QueryRegistry queryRegistry = new QueryRegistry(query);
		queryRegistry.setReturnLeafObjects(true);
		requestExchange.getIn().setBody(queryRegistry);
		
		Exchange result2 = template.send(URI_QUERY_REGISTRY, requestExchange);
		QueryResponse queryResponse = Exchanges.resultMessage(result2).getBody(QueryResponse.class);
		assertNotNull("Query Response is null, but should not be so.", queryResponse);
		assertEquals(Status.SUCCESS, queryResponse.getStatus());
		List<DocumentEntry> documentEntries = queryResponse.getDocumentEntries();
		assertEquals(1, documentEntries.size());
		
	}
	


}
