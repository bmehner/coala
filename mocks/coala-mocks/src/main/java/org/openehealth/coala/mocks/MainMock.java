package org.openehealth.coala.mocks;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.springframework.core.io.ClassPathResource;

public class MainMock {
	
	private int port;
	private JettyServer servletServer;
	
	public MainMock(int port) {
		this.port = port;
	}
	
	public void boot() throws Exception {
		ClassPathResource contextResource = new ClassPathResource("META-INF/spring/coala-mocks-context.xml");
		Servlet servlet = new CXFServlet();
        //log.info("Publishing services on port: ${port}")
        
        servletServer = new JettyServer();
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
        
        System.out.println("Starting Jetty servers with mocks deployed on port " + port + " ...");
        System.out.println("Press ctrl-c to stop.");
        servletServer.start();
	}
	
	public static void main(String[] args) throws Exception {		
		MainMock mainMock = new MainMock(8766);
		mainMock.boot();
	}
	
}
