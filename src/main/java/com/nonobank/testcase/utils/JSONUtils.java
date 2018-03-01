package com.nonobank.testcase.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils {
	
	private final static Pattern JSONARRAY_PATTERN = Pattern.compile("^\\[.*?\\]$");
    
    private final static Pattern JSONOBJECT_PATTERN = Pattern.compile("^\\{.*?\\}$");
    
    private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
	
    /**
	public static boolean isJsonObject(String jsonStr) throws JsonException{
		if(JSONOBJECT_PATTERN.matcher(jsonStr).matches()){
			
			try{
				JSONObject.parse(jsonStr);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
			return true;
		}else{
			return false;
		}
	}**/
	
	public static boolean isJsonArray(String jsonStr){
		if(JSONARRAY_PATTERN.matcher(jsonStr).matches()){
			
			try{
				JSONArray.parseArray(jsonStr);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
			return true;
		}else{
			return false;
		}
	}
	
	public static void compareStr(String key, String expectedStr, String actualStr, Map<String, String> map, List<String> list){
		if(SAVE_PATTERN.matcher(expectedStr).matches()){
			Matcher matcher = SAVE_PATTERN.matcher(expectedStr);
			if(matcher.find()){
				map.put(matcher.group(1), actualStr);
			}
		}else{
			if(!expectedStr.equals(actualStr)){
				list.add(key + " 预期结果是：" + expectedStr + ",但实际结果是：" + actualStr);
			}else{
				list.add(key + " 预期结果、实际结果相同，结果为：" + expectedStr);
			}
		}
	}
	
	public static void compareJsonObj(JSONObject expectedJsonObj, JSONObject actualJsonObj, Map<String, String> map, List<String> list){
		Set<String> keys = expectedJsonObj.keySet();
		
		keys.forEach(x->{
			Object expectedValue = expectedJsonObj.get(x);
			Object actualValue = actualJsonObj.get(x);
			
			if(expectedValue instanceof JSONObject){
				if(actualValue instanceof JSONObject){
					compareJsonObj((JSONObject)expectedValue, (JSONObject)actualValue, map, list);
				}else{
					list.add(x + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
				}
			}else if(expectedValue instanceof JSONArray){
				list.add("预期结果暂不支持数组, " + x + " : " + expectedValue);
			}else{
				compareStr(x, expectedJsonObj.getString(x), actualJsonObj.getString(x), map, list);
			}
		});
	}
	
	public static void main(String [] args){

		String str = "{\"a\":{\"b\":1}, \"c\":\"&{c}\", \"d\":12, \"e\":[1]}";
		
		String str1 = "{\"a\":{\"b\":1}, \"c\":1}";
		
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = new ArrayList<>();
		
		JSONObject jsonObj = JSONObject.parseObject(str);
		JSONObject jsonObj1 = JSONObject.parseObject(str1);
		compareJsonObj(jsonObj, jsonObj1, map, list);
		list.forEach(System.out::println);
		
		map.forEach((x,y)->{
			System.out.println(x + ":" + y);
		});
		
	}

}
