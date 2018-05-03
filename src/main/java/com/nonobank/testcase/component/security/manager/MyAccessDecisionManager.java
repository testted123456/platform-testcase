package com.nonobank.testcase.component.security.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.nonobank.testcase.component.remoteEntity.RemoteUser;
import com.nonobank.testcase.repository.RoleUrlPathRepository;
import com.nonobank.testcase.utils.IpAdrressUtil;

@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    private static Map<String, String> urlMap;
    
    public static final String NO_LOGIN = "no login";

    private static final String ANONYMOUS_USER = "anonymousUser";

    private static final String ROLE_ADMIN = "Admin";

    @Value("${ignore.urlPath}")
    String urlPathIgnore;

    @Value("${ignore.ip}")
    String ipIgnore;
    
    @Autowired
    RoleUrlPathRepository roleUrlPathRepository;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initUrlMap() {
    	List<Object[]> list = roleUrlPathRepository.findUrlAndRole();
    	
    	urlMap = new HashMap<>();
    	
    	list.forEach(x->{
    		urlMap.put(String.valueOf(x[0]), String.valueOf(x[1]));
    	});
    }

    public boolean checkIgnore(String value, String ignoreConf) {
        if (ignoreConf == null || ignoreConf.length() == 0) {
            return false;
        }
        String[] igonres = ignoreConf.split(",");
        for (String ingore : igonres) {
            if (value.equals(ingore) || value.endsWith(ingore)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断是否匿名用户
     *
     * @param authentication
     * @return
     */
    public static boolean isAnonymous(Authentication authentication) {
        if (authentication == null) {
            return true;
        } else {
            return authentication.getPrincipal().toString().equals(ANONYMOUS_USER);
        }
    }

    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
    	
    if(((FilterInvocation) object).getRequest().getMethod().equals("OPTIONS")){
    	return;
    }
    	
//      判断ip是否需要忽略
      String ip = IpAdrressUtil.getIpAdrress(((FilterInvocation) object).getRequest());
      if (checkIgnore(ip, ipIgnore)) {
          return;
      }
      
//      判断url是否需要忽略
      String url = ((FilterInvocation) object).getRequestUrl();
      
      if(url != null && url.startsWith("/webSocket")){
    	  return;
      }
      
      if (url.indexOf("?") != -1) {
          url = url.substring(0, url.indexOf("?"));
      }
      if (checkIgnore(url, urlPathIgnore)) {
          return;
      }

//    匿名用户即为非法访问
      if (isAnonymous(authentication)) {
          throw new AccessDeniedException(NO_LOGIN);
      }

//    获取urlmap
      if (urlMap == null || urlMap.keySet().size() == 0) {
//          throw new AccessDeniedException("");
    	  return;
      }

//      判断是否有相应的角色
      String needRole = (String) urlMap.get(url);
     
      if(null == needRole){
        	return;
      }
      
      for (GrantedAuthority ga : authentication.getAuthorities()) {
          if (ga.getAuthority().equals(ROLE_ADMIN) || needRole.trim().equals(ga.getAuthority().trim())) {
              return;
          }
      }
      

        if (configAttributes == null) {
            return;
        }

        throw new AccessDeniedException("");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
