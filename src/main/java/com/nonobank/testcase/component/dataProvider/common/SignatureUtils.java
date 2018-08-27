package com.nonobank.testcase.component.dataProvider.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.utils.dll.MD5Util;

public class SignatureUtils {

	/**
	 * @api {函数} getSignature_db() 获取签名
	 * @apiGroup SIGNATURE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getSignature_db()}
	 */
	public static void getSignature_db(){
	}

	public static String getSignature(String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		String appId = json.getString("appId");
		String appKey = "";
		if("nono".equals(appId)){
			appKey = "02c3acde8e4d9de5";
		}else if("mxd".equals(appId)){
			appKey = "2150d1db6f229ae2";
		}else if("bld".equals(appId)){
			appKey = "6cdee11944705fad";
		}else if("unifi".equals(appId)){
			appKey = "ea69ada8f3a31b99";
		}
		json.remove("appId");
		Set<String> set = json.keySet();
		List<String> list = new ArrayList<String>();
		for(String s : set){
			list.add(s);
		}
		Collections.sort(list);
		StringBuffer sb = new StringBuffer();
		for(String s : list){
			sb.append(s);
			sb.append("=");
			sb.append(json.getString(s));
			sb.append("&");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(appKey);
		String signature = MD5Util.toMD5(sb.toString());
		System.out.println(signature);
		return signature;
	} 
	
	public static JSONObject sign(JSONObject jsonObj){
		String key = "412fadsfoinhuc450f8jcnalzq08mfja";
		jsonObj.put("sign", key);
		String jsonStr = jsonObj.toJSONString();
		String sign = MD5Util.toMD5(jsonStr);
		jsonObj.put("sign", sign);
		return jsonObj;
	}
	
	public final static String md5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
	
	public static void main(String [] args){
		
		/*
		String str = "%7B%22application%22%3A%22NewUserLogin.Req%22%2C%22transDate%22%3A%2220180820%22%2C%22clientType%22%3A%2204%22%2C%22transTime%22%3A%22144356%22%2C%22dataRequestType%22%3A%22JSON%22%2C%22mobileSerialNum%22%3A%22E7C75A6FF1C987EE158BF0579C0554A200000000%22%2C%22userIP%22%3A%22192.168.1.120%22%2C%22appVersion%22%3A%22V3%22%2C%22customerId%22%3A%220000%22%2C%22latitude%22%3A%2231.215848%22%2C%22version%22%3A%223.8.0%22%2C%22sign%22%3A%22n85o3nd2romjnb06onivuq1m85k1qnfl%22%2C%22mobileNo%22%3A%2218011111111%22%2C%22token%22%3A%220000%22%2C%22longitude%22%3A%22121.530974%22%2C%22phone%22%3A%2218011111111%22%2C%22transLogNo%22%3A%22000006%22%2C%22appUser%22%3A%22bmzhangguibao%22%2C%22userName%22%3A%2218011111111%22%2C%22osType%22%3A%22iOS11.3.1%22%2C%22password%22%3A%220a074e5a9ad7ec76f6906c514c9f813809c9ee427525a8fca133a08c91d2a36d9ad74f29a163a902dd4162f9f7e82e24a7ea5cb5bdd62d4111fa165f179e9c3a40592fe3a7e527956ce9b8f7d9b27f116034581488c8d347df700db3e520b17b561d62143ce4c631c4acee26b0b3a9f167d0c8f8dd3936803d4c014a593d5750%22%2C%22loginCheckDevice%22%3A%221%22%2C%22areaCode%22%3A%22310115%22%7D";
		System.out.println(md5(str));*/
		
		String str1 = "{\"application\":\"NewUserLogin.Req\",\"transDate\":\"20180820\",\"clientType\":\"04\",\"transTime\":\"144356\",\"dataRequestType\":\"JSON\",\"mobileSerialNum\":\"E7C75A6FF1C987EE158BF0579C0554A200000000\",\"userIP\":\"192.168.1.120\",\"appVersion\":\"V3\",\"customerId\":\"0000\",\"latitude\":\"31.215848\",\"version\":\"3.8.0\",\"sign\":\"FB026208B4B1C71E54A4D1401854720E\",\"mobileNo\":\"18011111111\",\"token\":\"0000\",\"longitude\":\"121.530974\",\"phone\":\"18011111111\",\"transLogNo\":\"000006\",\"appUser\":\"bmzhangguibao\",\"userName\":\"18011111111\",\"osType\":\"iOS11.3.1\",\"password\":\"0a074e5a9ad7ec76f6906c514c9f813809c9ee427525a8fca133a08c91d2a36d9ad74f29a163a902dd4162f9f7e82e24a7ea5cb5bdd62d4111fa165f179e9c3a40592fe3a7e527956ce9b8f7d9b27f116034581488c8d347df700db3e520b17b561d62143ce4c631c4acee26b0b3a9f167d0c8f8dd3936803d4c014a593d5750\",\"loginCheckDevice\":\"1\",\"areaCode\":\"310115\"}";
		str1="{\"appUser\":\"bmzhangguibao\",\"appVersion\":\"V3\",\"transTime\":\"144356\",\"mobileSerialNum\":\"E7C75A6FF1C987EE158BF0579C0554A200000000\",\"latitude\":\"31.215848\",\"sign\":\"28c370aa3d177bd23c62613860bbbb69\",\"mobileNo\":\"18011111111\",\"userName\":\"18011111111\",\"version\":\"3.8.0\",\"token\":\"0000\",\"password\":\"0a074e5a9ad7ec76f6906c514c9f813809c9ee427525a8fca133a08c91d2a36d9ad74f29a163a902dd4162f9f7e82e24a7ea5cb5bdd62d4111fa165f179e9c3a40592fe3a7e527956ce9b8f7d9b27f116034581488c8d347df700db3e520b17b561d62143ce4c631c4acee26b0b3a9f167d0c8f8dd3936803d4c014a593d5750\",\"areaCode\":\"310115\",\"clientType\":\"04\",\"application\":\"NewUserLogin.Req\",\"phone\":\"18011111111\",\"transDate\":\"20180820\",\"customerId\":\"0000\",\"osType\":\"iOS11.3.1\",\"userIP\":\"192.168.1.120\",\"dataRequestType\":\"JSON\",\"transLogNo\":\"000006\",\"loginCheckDevice\":\"1\",\"longitude\":\"121.530974\"}";
			
		JSONObject jsonObj = JSONObject.parseObject(str1);
		String sign = jsonObj.getString("sign");
		String key = "n85o3nd2romjnb06onivuq1m85k1qnfl";
		str1=str1.replace(sign, key);
		try {
			str1 = URLEncoder.encode(str1, "UTF-8");
			System.out.println(str1);
			System.out.println(md5(str1).toUpperCase());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String log="%7B%22application%22%3A%22NewUserLogin.Req%22%2C%22transDate%22%3A%2220180820%22%2C%22clientType%22%3A%2204%22%2C%22transTime%22%3A%22144356%22%2C%22dataRequestType%22%3A%22JSON%22%2C%22mobileSerialNum%22%3A%22E7C75A6FF1C987EE158BF0579C0554A200000000%22%2C%22userIP%22%3A%22192.168.1.120%22%2C%22appVersion%22%3A%22V3%22%2C%22customerId%22%3A%220000%22%2C%22latitude%22%3A%2231.215848%22%2C%22version%22%3A%223.8.0%22%2C%22sign%22%3A%22n85o3nd2romjnb06onivuq1m85k1qnfl%22%2C%22mobileNo%22%3A%2218011111111%22%2C%22token%22%3A%220000%22%2C%22longitude%22%3A%22121.530974%22%2C%22phone%22%3A%2218011111111%22%2C%22transLogNo%22%3A%22000006%22%2C%22appUser%22%3A%22bmzhangguibao%22%2C%22userName%22%3A%2218011111111%22%2C%22osType%22%3A%22iOS11.3.1%22%2C%22password%22%3A%220a074e5a9ad7ec76f6906c514c9f813809c9ee427525a8fca133a08c91d2a36d9ad74f29a163a902dd4162f9f7e82e24a7ea5cb5bdd62d4111fa165f179e9c3a40592fe3a7e527956ce9b8f7d9b27f116034581488c8d347df700db3e520b17b561d62143ce4c631c4acee26b0b3a9f167d0c8f8dd3936803d4c014a593d5750%22%2C%22loginCheckDevice%22%3A%221%22%2C%22areaCode%22%3A%22310115%22%7D";
		System.out.println(str1.equals(log));
	}

}