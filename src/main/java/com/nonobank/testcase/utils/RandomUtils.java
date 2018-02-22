package com.nonobank.testcase.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.testng.collections.Lists;

public class RandomUtils {
	private static RandomUtils instance = new RandomUtils();
	private static Random random = new Random();
	private static String NUMBERS = "0123456789";
	private static Map<String, List<String>> MOBILE_OPERATOR_MAP = new HashMap<String, List<String>>();
	static {
		MOBILE_OPERATOR_MAP.put("CM", Lists.newArrayList("137", "138", "139"));
		MOBILE_OPERATOR_MAP.put("CU", Lists.newArrayList("186", "187", "189", "188"));
		MOBILE_OPERATOR_MAP.put("CT", Lists.newArrayList("150", "151", "152", "153"));
	}
	private static List<String> USERNAME_PREFIX = Lists.newArrayList("ZZZ", "VVV", "ZTV", "ZTL");
	private static List<String> USERNAME_POSTFIX = Lists.newArrayList("tb", "sb", "nb");

	public static RandomUtils getInstance() {
		return instance;
	}

	public RandomUtils() {
	}

	public double generateRandomNum(){
		double num= 0.8999999999999999*Math.random()+0.1;
		return num;
	}
	
	public String generateMobilePhoneNumber() {
		return generateMobilePhoneNumber(11);
	}

	public String generateMobilePhoneNumber(int digital) {
		StringBuffer sb = new StringBuffer();
		// todo 目前默认是CU
		String mobile_prefix = MOBILE_OPERATOR_MAP.get("CU").get(random.nextInt(MOBILE_OPERATOR_MAP.get("CU").size()));
		sb.append(mobile_prefix);
		for (int i = 0; i < digital - 3; i++) {
			sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
		}

		return sb.toString();
	}

	public String generateUserName() {
		StringBuffer sb = new StringBuffer();
		String prefix = USERNAME_PREFIX.get(random.nextInt(USERNAME_PREFIX.size()));
		sb.append(prefix);
		for (int i = 0; i < 3; i++) {
			sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
		}
		sb.append(USERNAME_POSTFIX.get(random.nextInt(USERNAME_POSTFIX.size())));
		return sb.toString();
	}

	public int generateAge() { // more thank 18 and less than 40
		int age = 18;
		age = age + random.nextInt(40 - 18);
		return age;
	}

	public int getBirthYear(int age) {
		return Calendar.getInstance().get(Calendar.YEAR) - age;
	}

	public int getBirthMonth() {
		return random.nextInt(13);
	}

	public int getBirthDay(int month, int year) {
		List<Integer> month1 = Lists.newArrayList(1, 3, 5, 7, 8, 10, 12);
		List<Integer> month2 = Lists.newArrayList(4, 6, 9, 11);
		if (month == 2) {
			if (year % 4 == 0 && year % 100 != 0) {
				return random.nextInt(28); // 闰年
			} else {
				return random.nextInt(29);
			}
		}
		if (month1.contains(month)) {
			return random.nextInt(31);
		}
		if (month2.contains(month)) {
			return random.nextInt(30);
		}
		return random.nextInt(28);
	}

	public String generateReferenceId() {
		return String.valueOf(random.nextInt(1000000));
	}

	public String generateQQNumber() {
		return String.valueOf(random.nextInt(100000));
	}

	public String generatePassword() {
		return "it789123";
	}

	public String generateRandomValue(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
		}
		return sb.toString();
	}

	public String generateUniqueRandomValue() {
		Long value = System.currentTimeMillis();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return value.toString();
	}
	
	public String generateRandomEmail(){
		String mobile=generateMobilePhoneNumber();
		String email=mobile+"@163.com";
		System.out.println(email);
		return email;	
	}

	public String generateAmericaMobile() {
		return generateMobilePhoneNumber(10);
	}

}
