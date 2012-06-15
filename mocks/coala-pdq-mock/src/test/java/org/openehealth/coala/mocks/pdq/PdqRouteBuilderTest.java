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

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

public class PdqRouteBuilderTest extends AbstractJUnit4SpringContextTests {

	@Produce(uri ="pdq-iti21://localhost:8765")
	private ProducerTemplate template;
	
	@Autowired
	private CamelContext camelContext;
	
	@Before
	public void setup() throws Exception {
		camelContext.getRouteDefinitions().get(0).adviceWith(camelContext, new RouteBuilder() {
	        @Override
	        public void configure() throws Exception {
	            // intercept sending to mock:foo and do something else
	            interceptFrom(uri)
	                    .skipSendToOriginalEndpoint()
	                    .to("log:foo")
	                    .to("mock:advised");
	        }
	    });
	}
}
