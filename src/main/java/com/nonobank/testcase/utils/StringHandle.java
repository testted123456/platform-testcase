package com.nonobank.testcase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHandle {

	public static int removeDuplicates(int[] nums) {
		if (nums.length < 1) {
			return nums.length;
		}
		int slow = 1;
		for (int fast = 1; fast < nums.length; fast++) {
			if (nums[fast] != nums[slow - 1]) {
				nums[slow++] = nums[fast];
			}
		}
		return slow;
	}

	// 取出字符串中的数字
	public static double numFromString(String a) {
		String regEx = "[^0-9\\.]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);
		String num_str = m.replaceAll("").trim();
		double num = Double.parseDouble(num_str);
		return num;
	}
	public static String numFromString_str(String a){
		String regEx = "[^0-9\\.]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);
		String num_str = m.replaceAll("").trim();
		return num_str;
	}
}
