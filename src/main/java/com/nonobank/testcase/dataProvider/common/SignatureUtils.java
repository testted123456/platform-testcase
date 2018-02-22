package com.nonobank.testcase.dataProvider.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.dataProvider.annotation.Info;
import com.nonobank.testcase.dataProvider.annotation.Param;
import com.nonobank.testcase.dataProvider.annotation.Return;
import com.nonobank.testcase.utils.MD5Util;

public class SignatureUtils {
	
	@Info(name="getSignature_db",desc="获取签名")
	@Param(type={},name={},desc={})
	@Return(type="String",desc="获取签名")
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

}
