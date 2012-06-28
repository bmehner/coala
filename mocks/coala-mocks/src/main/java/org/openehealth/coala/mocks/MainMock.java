package org.openehealth.coala.mocks;

import org.apache.camel.spring.Main;

public class MainMock {
	
	private Main main;
	
	public static void main(String[] args) throws Exception {
		MainMock mainMock = new MainMock();
		mainMock.boot();
	}
	
	public void boot() throws Exception {
		main = new Main();
		main.setApplicationContextUri("classpath:/META-INF/spring/coala-mocks-context.xml");
		main.enableHangupSupport();
		main.run();
	}

}
