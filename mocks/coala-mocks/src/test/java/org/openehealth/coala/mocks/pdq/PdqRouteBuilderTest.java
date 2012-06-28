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
package org.openehealth.coala.mocks.pdq;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.RSP_K21;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PdqRouteBuilderTest extends CamelSpringTestSupport implements PDQTestConstants {

	@Test
	public void testRouteSetup() throws Exception {
		Exchange exchange = new DefaultExchange(context);
		exchange.getIn().setBody(VALID_REQUEST_HEADER);
		@SuppressWarnings("unchecked")
		MessageAdapter<RSP_K21> response = (MessageAdapter<RSP_K21>)template.requestBody("pdq-iti21://localhost:8765?audit=false", 
				VALID_REQUEST_HEADER);
		
		RSP_K21 hapiMessage = (RSP_K21) response.getHapiMessage();
		
		assertEquals(4, hapiMessage.getRSP_K21_QUERY_RESPONSEReps());
		
	}

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("classpath:/META-INF/spring/coala-mocks-context.xml");
	}
}
