package com.nonobank.testcase.component.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;



/**
 * 配置静态资源映射
 *
 * @author sam
 * @since 2017/7/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ApplicationHome home = new ApplicationHome(this.getClass());
        String funcDirPath = home.getDir().getPath() + "/" + "funcdoc/";
        registry.addResourceHandler("/func/**").addResourceLocations("file:" + funcDirPath);
        registry.addResourceHandler("/image/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
