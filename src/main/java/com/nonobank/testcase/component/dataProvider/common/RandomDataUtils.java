package com.nonobank.testcase.component.dataProvider.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import com.nonobank.testcase.component.dataProvider.annotation.Info;
import com.nonobank.testcase.component.dataProvider.annotation.Param;
import com.nonobank.testcase.component.dataProvider.annotation.Return;

public class RandomDataUtils {

	@Info(name="generateRandomValue",desc="返回指定长度的随机数")
	@Param(type={"String"},name={"length"},desc={"随机数的长度"})
	@Return(type="String",desc="随机数")

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

	@Info(name="generateRandomStr",desc="返回数组中任意字符串")
	@Param(type={"String"},name={"arr"},desc={"任意数组"})
	@Return(type="String",desc="任意字符串")

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