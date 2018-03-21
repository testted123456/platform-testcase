package com.nonobank.testcase.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nonobank.testcase.component.ws.WebSocket;

@Controller
@RequestMapping(value="test")
@CrossOrigin(origins = "*", maxAge = 3600)
@EnableAsync
public class TestController {
	
	@Autowired
	WebSocket webSocket;
	
	@GetMapping(value="test")
//	@Async
	public String test(){
		/**
		 System.out.println("线程名称："+Thread.currentThread().getName() + " be ready to read data!");
	        try {
	            Thread.sleep(1000 * 5);
	            System.out.println("---------------------》》》无返回值延迟3秒：");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        return "已进入到异步";
	        **/
		if(null == webSocket){
			System.out.println("xxxxxxxx");
		}
		
		return null;
	}
	
	@RequestMapping("/hello")
    public String helloHtml(HashMap<String, Object> map) {
        map.put("hello", "欢迎进入HTML页面");
        return "/hello";
    }

}
