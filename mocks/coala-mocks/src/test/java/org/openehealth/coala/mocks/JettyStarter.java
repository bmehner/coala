package org.openehealth.coala.mocks;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JettyStarter {
	/**
	 * Starts a Jetty Server with a CXFServlet listening on the given port. The Servlet uses
	 * the specified Spring configuration and starts the context during serlet start up.
	 * @param port Port to which the Jetty Server will listen.
	 * @param springConfig path to classpath resource without the leading "classpath:"
	 * @return The instantiated Spring application context. 
	 */
	public static ApplicationContext startJettyServer(int port, String springConfig) {
		ClassPathResource contextResource = new ClassPathResource(springConfig);
		Servlet servlet = new CXFServlet();
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
        
        ServletContext servletContext = servlet.getServletConfig().getServletContext();
		ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        
        return appContext;
	}
}
