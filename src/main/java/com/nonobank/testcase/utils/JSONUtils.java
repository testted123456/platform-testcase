package com.nonobank.testcase.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {
	
	private final static Pattern JSONARRAY_PATTERN = Pattern.compile("^\\[.*?\\]$");
    
    private final static Pattern JSONOBJECT_PATTERN = Pattern.compile("^\\{.*?\\}$");
    
    private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
	
	public static boolean isJsonObject(String jsonStr) {
		if(JSONOBJECT_PATTERN.matcher(jsonStr).matches()){
			try{
				JSONObject.parse(jsonStr);
			}catch(Exception e){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
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
	
	/**
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
	}**/
	
	public static JSONArray compareJsonArray(JSONArray jsonArray1, JSONArray jsonArray2){
		JSONArray reslutJsonArray = new JSONArray();
		
		int size1 = jsonArray1.size();
		int size2 = jsonArray2.size();
		
		if(size1>0 && size2>0){
			Object obj1 = jsonArray1.get(0);
			Object obj2 = jsonArray2.get(0);
			
			if(!obj1.getClass().equals(obj2.getClass())){
				 reslutJsonArray.add("元素类型不同");
				 return reslutJsonArray;
			}else{
				if(obj1 instanceof JSONObject){
					reslutJsonArray.add(compareJsonObj((JSONObject)obj1, (JSONObject)obj2));
				}else if(obj1 instanceof JSONArray){
					reslutJsonArray.add(compareJsonArray((JSONArray)obj1, (JSONArray)obj2));
				}
			}
		}
		
		return reslutJsonArray;
	}
	
	public static JSONObject compareJsonObj(JSONObject jsonValue1, JSONObject jsonValue2){
		JSONObject resultJson = new JSONObject();
		Set<String> keys1 = jsonValue1.keySet();
		Set<String> keys2 = jsonValue2.keySet();
		
		//已删除
		List<String> delKeys = keys1.stream().filter(x->{
			return !keys2.contains(x);
		}).collect(Collectors.toList());
		
		delKeys.forEach(x->{
			resultJson.put(x, "已删除");
		});
		
		//新增
		List<String> addKeys = keys2.stream().filter(x->{
			return !keys1.contains(x);
		}).collect(Collectors.toList());
		
		addKeys.forEach(x->{
			resultJson.put(x, "新增");
		});
		
		//相同
		List<String> sameKeys = keys2.stream().filter(x->{
			return keys1.contains(x);
		}).collect(Collectors.toList());
		
		sameKeys.forEach(x->{
			Object var1 = jsonValue1.get(x);
			Object var2 = jsonValue2.get(x);
			
			if(!var1.getClass().equals(var2.getClass())){
				resultJson.put(x, "类型不同");
			}else if(var1 instanceof JSONObject && var2 instanceof JSONObject){
				JSONObject subResultJson = compareJsonObj((JSONObject)var1, (JSONObject)var2);
				if(subResultJson.size() != 0){
					resultJson.put(x, subResultJson);
				}
			}else if(var1 instanceof JSONArray && var2 instanceof JSONArray){
				JSONArray resultJsonArray = compareJsonArray((JSONArray)var1, (JSONArray)var2);
				if(null != resultJsonArray && resultJsonArray.size()>0){
					resultJson.put(x, resultJsonArray);
				}
			}
		});
		
		return resultJson;
	}
	
	/**
	 * 格式化json
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String format(String jsonStr) throws JsonParseException, JsonMappingException, IOException {
		 ObjectMapper mapper = new ObjectMapper();
		 Object obj = mapper.readValue(jsonStr, Object.class);
		 return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}
	
	/**
	 * 格式化json
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String format(Object obj) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValueAsString(obj);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}
	
	public static void main(String [] args) throws Exception{

		
		String str = "{\"a\":{\"b\":1}, \"c\":\"&{c}\", \"d\":12, \"e\":[{\"a\":1},{\"a\":1}]}";
		String str1 = "{\"aaa\":{\"w\":1}, \"c\":\"&{c}\", \"d\":12, \"e\":[{\"c\":2}]}";
		/**
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
		**/
		JSONObject jsonObj = 
		compareJsonObj(JSONObject.parseObject(str), JSONObject.parseObject(str1));
		
		System.out.println(format(jsonObj));
	}

}
