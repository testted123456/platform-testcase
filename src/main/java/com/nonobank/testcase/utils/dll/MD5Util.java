package com.nonobank.testcase.utils.dll;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Util {
	public static Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	/**
	 * 
	 * @Title: toMD5
	 * @Description: TODO
	 * @param str
	 * @return md5Str
	 */
	public static String toMD5(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			return null;
		}
		md5.update(str.getBytes());
		byte[] md5Bytes = md5.digest();
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 
	 * @Title: equalsMD5
	 * @Description: TODO
	 * @param str
	 * @param md5
	 * @return result
	 */
	public static boolean equalsMD5(String str, String md5) {
		if (str.isEmpty() || md5.isEmpty()) {
			return false;
		}
		return toMD5(str).toUpperCase().equals(md5.toUpperCase());
	}
}
