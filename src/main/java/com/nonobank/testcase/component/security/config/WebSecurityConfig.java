package com.nonobank.testcase.component.security.config;

import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AccessDecisionManager accessDecisionManager;
    
    @Resource
    AuthenticationEntryPoint authenticationEntryPoint;
    
    @Resource
    AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	
    	http.csrf().disable().
//    	anonymous().disable().
    	authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager);
    	
    	//处理没有登陆用户
    	http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    	
    	//没有权限
    	http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    	
//        http.csrf().disable().authorizeRequests().antMatchers("/*").authenticated()
//                .accessDecisionManager(accessDecisionManager);

//        http.csrf().disable().authorizeRequests()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/index").hasRole("Admin").accessDecisionManager(accessDecisionManager);
    }


    /**
     * 权限不通过的处理
     */
    /**
    public static class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication Failed: " + authException.getMessage());
        }
    }**/

}

