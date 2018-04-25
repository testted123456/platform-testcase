package com.nonobank.testcase.component.security.manager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

//		response.setStatus(200);
//		response.setHeader("content-type", "application/json;charset=UTF-8");
//		OutputStream os = response.getOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(os);
//		oos.writeObject("{\"code\":1008, \"msg\":\"insuffcient rights\"}");
//		oos.close();
		response.setStatus(403);
	}

}
