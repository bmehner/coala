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
package org.openehealth.coala.mocks;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Ignore
public class ServerBaseTest {

	protected static final String URI = "xds-iti41://localhost:8766/mockrepo?audit=false";
	
	protected static WebApplicationContext appContext;
	protected static ProducerTemplate producerTemplate;
	protected static CamelContext camelContext;
	
	@BeforeClass
	public static void startServer() {
		CXFServlet servlet = new CXFServlet();
		startJettyServer(servlet);
		ServletContext servletContext = servlet.getServletConfig().getServletContext();
		appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		producerTemplate = (ProducerTemplate) appContext.getBean("template");
		camelContext = (CamelContext) appContext.getBean("camelContext"); 
	}
	
	public static void startJettyServer(Servlet servlet) {
		ClassPathResource contextResource = new ClassPathResource("META-INF/spring/coala-mocks-context.xml");
        
        int port = 8766;
        //log.info("Publishing services on port: ${port}")
        
        JettyServer servletServer = new JettyServer();
        try {
			servletServer.setContextResource(contextResource.getURI().toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        servletServer.setPort(port);
        servletServer.setContextPath("");
        servletServer.setServletPath("/*");
        servletServer.setServlet(servlet);
        servletServer.setSecure(false);
        
        servletServer.start();
	}
}
