package com.nonobank.testcase.component.security.manager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
//		response.setStatus(200);
//		response.setHeader("content-type", "application/json;charset=UTF-8");
//		OutputStream os = response.getOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(os);
//		oos.writeObject("{\"code\":1007, \"msg\":\"login error\"}");
//		oos.close();
		response.setStatus(401);

	}

}
