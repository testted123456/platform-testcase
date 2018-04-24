package com.nonobank.testcase.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

	/**
	 * 获取登陆用户
	 * @return
	 */
	public static String getUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(null != authentication){
			Object principal = authentication.getPrincipal();
			return principal == null ? null : principal.toString();
		}
		
		return null;
	}
}
