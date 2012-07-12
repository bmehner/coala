package org.openehealth.coala.mocks;

import org.apache.camel.spring.Main;
import org.openehealth.ipf.commons.ihe.ws.server.JettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainMock {
	
	private Main main;
	
	public static void main(String[] args) throws Exception {
		MainMock mainMock = new MainMock();
		mainMock.boot();
	}
	
	public void boot() throws Exception {
		System.out.println("Starting mocks...\nPress ctrl-c to exit.");
		ApplicationContext context = JettyStarter.startJettyServer(8766, "/META-INF/spring/coala-mocks-context.xml");
		main = new Main();
		main.setApplicationContextUri("classpath:/META-INF/spring/coala-mocks-context.xml");
		main.enableHangupSupport();
		main.run();
//		new ClassPathXmlApplicationContext("classpath:/META-INF/spring/coala-mocks-context.xml");
//		Thread.currentThread().join();
	}

}
