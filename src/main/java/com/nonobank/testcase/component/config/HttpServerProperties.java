package com.nonobank.testcase.component.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "httpServer")
public class HttpServerProperties {
	
	private String interfaceServer;

	public String getInterfaceServer() {
		return interfaceServer;
	}

	public void setInterfaceServer(String interfaceServer) {
		this.interfaceServer = interfaceServer;
	}
	
}
