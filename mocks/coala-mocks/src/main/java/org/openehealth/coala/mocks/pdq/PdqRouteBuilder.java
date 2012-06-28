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
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators;

public class PdqRouteBuilder extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		onException(Exception.class)
			.handled(true)
			.log(LoggingLevel.ERROR, "${exception.message}");
		
		from("pdq-iti21://0.0.0.0:8765?audit=false")
			.setExchangePattern(ExchangePattern.InOut)
			.log(LoggingLevel.INFO, "Message Received")
			.process(PixPdqCamelValidators.iti21RequestValidator()).id("requestValidator")
			.process(new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(
							"MSH|^~\\&|MESA_PD_SUPPLIER|PIM|MESA_PD_CONSUMER|MESA_DEPARTMENT|20090901140929||RSP^K22^RSP_K21|356757|P|2.5\n" +
							"MSA|AA|1305506339\n" +
							"QAK|1486133081|OK\n" +
							"QPD|IHE PDQ Query|1486133081|@PID.11.3^KÃ¶ln\n" +
							"PID|1||78001^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI||Norris^Chuck||19400101|M|||1600 Pennsylvania Avenue NW^^Washington^DC^20500^US||202-456-1111\n" +
							"PID|2||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||Parker^Peter^Benjamin||19400101|M|||20 Ingram Street^^Forest Hills^New York^11375^US||\n" +
							"PID|3||79653^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||Kent^Clark||19400101|M|||344 Clinton Street^Apt. #3B^Metropolis^Delaware^19907^US||\n" +
							"PID|4||79233^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI||Wayne^Bruce||19400101|M|||Wayne Manor^Bristol Hills^Gotham City^New Jersey^07309^US||\n"
							);
					
				}
			})
			.process(PixPdqCamelValidators.iti21ResponseValidator()).id("responseValidator");

	}

}
