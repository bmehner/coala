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

import static org.junit.Assert.assertEquals;
import static org.openehealth.coala.mocks.xds.XdsTestUtils.createProvideAndRegisterDocumentSet;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.coala.mocks.JettyStarter;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.springframework.context.ApplicationContext;

public class XdsRouteBuilderTest {

	private static final int PORT = 8766;
	private static final String URI = "xds-iti41://localhost:" + PORT + "/mockrepo?audit=false";

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
	public void testRouteSetup() throws Exception {
		Exchange exchange = new DefaultExchange(context);
		
		ProvideAndRegisterDocumentSet request = createProvideAndRegisterDocumentSet();
		exchange.getIn().setBody(request);
		Exchange result = template.send(URI, exchange);
		Response response = Exchanges.resultMessage(result).getBody(Response.class);
		assertEquals(Status.SUCCESS, response.getStatus());
	}
	
	


}
