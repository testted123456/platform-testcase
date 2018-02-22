package com.nonobank.testcase.component.executor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.utils.InvokeUtils;

@Component
public class VariableHandler {
	
	public static Logger logger = LoggerFactory.getLogger(VariableHandler.class);

	private final static Pattern METHODNAME_PATTERN = Pattern.compile("^\\$\\{(\\w+)\\(.*?\\)\\}$");

    private final static Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(\\w*\\[*\\d*\\]*\\.*)+\\}");

    private final static Pattern JSONARRAY_PATTERN = Pattern.compile("^\\[.*?\\]$");
    
    private final static Pattern JSONOBJECT_PATTERN = Pattern.compile("^\\{.*?\\}$");

    private final static Pattern DB_PATTERN = Pattern.compile("\\$\\{(\\w+DBS)\\}");

    private final static Pattern NUM_PATTERN = Pattern.compile("(\"number\\[(\\d*\\.?\\d*)\\]\")");

    private final static Pattern LIST_PATTERN = Pattern.compile("(\"list\\[(\\[.*\\])\\]\")");
    
    private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
    
    public static void compareStr(String key, String expectedStr, String actualStr, Map<String, Object> map, List<String> list){
		if(SAVE_PATTERN.matcher(expectedStr).matches()){
			Matcher matcher = SAVE_PATTERN.matcher(expectedStr);
			if(matcher.find()){
				map.put(matcher.group(1), actualStr);
				list.add("true: 已保存变量" + matcher.group(1) + " ，变量值为：" + actualStr);
			}
		}else{
			if(!expectedStr.equals(actualStr)){
				list.add("false: " + key + " 预期结果是：" + expectedStr + ",但实际结果是：" + actualStr);
			}else{
				list.add("true: " + key + " 预期结果、实际结果相同，结果为：" + expectedStr);
			}
		}
	}
	
	public static void compareJsonObj(JSONObject expectedJsonObj, JSONObject actualJsonObj, Map<String, Object> map, List<String> list){
		Set<String> keys = expectedJsonObj.keySet();
		
		keys.forEach(x->{
			Object expectedValue = expectedJsonObj.get(x);
			Object actualValue = actualJsonObj.get(x);
			
			if(expectedValue instanceof JSONObject){
				if(actualValue instanceof JSONObject){
					compareJsonObj((JSONObject)expectedValue, (JSONObject)actualValue, map, list);
				}else{
					list.add("false: " + x + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
				}
			}else if(expectedValue instanceof JSONArray){
				list.add("false: 预期结果暂不支持数组, " + x + " : " + expectedValue);
			}else{
				compareStr(x, expectedJsonObj.getString(x), actualJsonObj.getString(x), map, list);
			}
		});
	}
    
	/**
	 * 替换变量
	 * @param map
	 * @param variable
	 * @return
	 */
    public Map<Boolean, String>  handleVariable(Map<String, Object> map, String variable){
    	Map<Boolean, String> resultMap = new HashMap<Boolean, String>();
    	
    	Matcher matcher = VARIABLE_PATTERN.matcher(variable);
    	
    	while(matcher.find()){
    		String key = matcher.group();
    		logger.info("开始替换变量" + key);
    		
    		Object value = map.get(key);
    		String replaceValue = null;
    		
    		if(null == value){
    			logger.error("变量" + key + "不在上下文中");
    			resultMap.put(false, "变量" + key + "不在上下文中");
    			break;
    		}else{
    			replaceValue = String.valueOf(value);
    			logger.info("变量" + key + "值为：" + replaceValue);
    		}
    		
    		variable = matcher.replaceAll(replaceValue);
    		matcher = VARIABLE_PATTERN.matcher(variable);
    	}
    	
    	if(!resultMap.containsKey(false)){
    		resultMap.put(true, variable);
    	}
    	
    	return resultMap;
    }
    
    /**
     * 替换方法
     * @param method
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public Map<Boolean, String> handleMethod(String method){
    	Map<Boolean, String> map = new HashMap<Boolean, String>();
    	Matcher matcher = METHODNAME_PATTERN.matcher(method); 
    	
    	String funcName = null;
    	String args = null;
    	List<String> listArgs = new ArrayList<String>();
    	
    	while(matcher.find()){
    		funcName = matcher.group(1);
    		args = matcher.group(2);
    		logger.info("开始替换函数" + funcName + " ,参数为：" + args);
    	}
    	
    	Pattern  pattern = Pattern.compile("(\".+?\")");
    	
    	matcher = pattern.matcher(args);
    	
    	while(matcher.find()){
    		listArgs.add(matcher.group(1)); 
    	}
    	
    	String [] array = listArgs.toArray(new String[listArgs.size()]);
    	String result = null;
    	
		try {
			result = InvokeUtils.invokeMethod(funcName, array);
			logger.info("函数" + funcName + "执行结果为：" + result);
			map.put(true, result);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			logger.error("反射调用方法抛异常: InstantiationException");
			map.put(false, "InstantiationException");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.error("反射调用方法抛异常: IllegalAccessException");
			map.put(false, "IllegalAccessException");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			logger.error("反射调用方法抛异常: NoSuchMethodException");
			map.put(false, "NoSuchMethodException");
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			logger.error("反射调用方法抛异常: SecurityException");
			map.put(false, "SecurityException");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logger.error("反射调用方法抛异常: IllegalArgumentException");
			map.put(false, "IllegalArgumentException");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("反射调用方法抛异常: InvocationTargetException");
			map.put(false, "InvocationTargetException");
			e.printStackTrace();
		}
    	return map;
    }
    
    public Map<Boolean, String> handleReqestBody(Map<String, Object> map, String requestBody){
    	logger.info("开始处理请求消息");
    	return handleVariable(map, requestBody);
    }
    
    public List<String> handleResponseBody(Map<String, Object> map, String expectedResponseBody, String actualResponseBody, String responseBodyType){
    	logger.info("开始处理响应消息");
    	
    	List<String> list = new ArrayList<String>();
    	
    	if("1".equals(responseBodyType)){//text响应消息
    		Matcher matcher = SAVE_PATTERN.matcher(expectedResponseBody);
    		
    		if(matcher.matches()){
    			if(matcher.find()){
    				map.put(matcher.group(1), actualResponseBody);
    			}
    			
    			list.add("true: 已保存变量：" + matcher.group(1) + " ，变量值为：" + actualResponseBody);
    			
    			return list;
    		}
    		
    		if(expectedResponseBody.equals(actualResponseBody)){
    			list.add("true: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
    		}else{
    			list.add("false: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
    		}
    		
    		return list;
    	}
    	
    	//json响应消息
    	if(JSONOBJECT_PATTERN.matcher(expectedResponseBody).matches()){
    		if(JSONOBJECT_PATTERN.matcher(actualResponseBody).matches()){
    			compareJsonObj(JSONObject.parseObject(expectedResponseBody), JSONObject.parseObject(expectedResponseBody), map, list);
    		}else{
    			logger.error("响应消息不是json格式");
    			list.add("响应消息不是json格式");
    		}
    	}
    	
    	return list;
    }
    
    public String handleAssertions(Map<String, Object> map, String assertion){
//    	assertion = handleVariable(map, assertion);
//    	assertion = handleMethod(assertion);
    	return assertion;
    }
    
    public static void main(String [] args){
    	/**
    	String str = "${getOneField_db(\"SELECT id FROM user_info WHERE  mobile_num=${username}   ORDER BY id DESC LIMIT 1;\", \"xxxx\")}";

    	VariableHandler var = new VariableHandler();
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("${bad123}", "www");
    	map.put("${xxxwwwyyyy123}", "zzz");
    	
    	System.out.println(var.replaceVariable(map, str));
    	System.out.println(str);
        Matcher matcher = METHODNAME_PATTERN.matcher(str);
        
        Pattern pattern =
        Pattern.compile("^\\$\\{(\\w+)\\((.*?)\\)\\}$");
        
        Matcher matcher = pattern.matcher(str);
        
        String vv = null;
        
        while(matcher.find()){
        	System.out.println(matcher.group(0));
        	System.out.println(matcher.group(1));
        	System.out.println(matcher.group(2));
        	vv = matcher.group(2);
        }
        
        pattern =
                Pattern.compile("(\".+?\")");
        
        matcher = pattern.matcher(vv);
        System.out.println("vv");
        
        while(matcher.find()){
        	System.out.println(matcher.group(1));
        }**/
    	String jsonStr = "{\"a\":1, \"b\":[{\"b1\":1}], \"c\":{\"d\":1}}";
    	System.out.println("");
    }
}
