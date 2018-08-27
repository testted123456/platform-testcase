package com.nonobank.testcase.component.dataProvider.common;

import java.util.Random;

public class IPUtils {
	
	/**
	 * @api {函数} getRandomIP() 生成随机ip
	 * @apiGroup RANDOM
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRandomIP()}
	 */
	public static String getRandomIP(){
		Random random = new Random();
		return "192.168." + String.valueOf(random.nextInt(253) + 1) + "." + String.valueOf(random.nextInt(253) + 1);
	}
	
}
