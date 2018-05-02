package com.nonobank.testcase.component.dataProvider.common;

import java.util.Random;

public class RandomDataUtils {

	/**
	 * @api {函数} generateRandomValue("length") 生成指定长度随机数
	 * @apiGroup RANDOM
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} length 随机数的长度
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateRandomValue("length")}
	 */
	public static String generateRandomValue(String length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		String NUMBERS = "0123456789";
		int len = Integer.valueOf(length);

		for (int i = 0; i < len; i++) {
			sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
		}
		return sb.toString();
	}

	/**
	 * @api {函数} generateRandomStr("arr") 随机返回数组中一个元素
	 * @apiGroup RANDOM
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} arr 任意数组
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateRandomStr("aaa,bbbb,ccc,ddd")}
	 */
	public static String generateRandomStr(String arr) {
		String str = arr.substring(1,arr.length()-1);
		String[] s = str.split(",");
		String random = "";
		int index = (int) (Math.random() * s.length);
		random = s[index];
		return random;
	}
}