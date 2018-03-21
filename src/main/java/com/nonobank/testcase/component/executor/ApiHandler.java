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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.utils.dll.InvokeUtils;

@Component
public class ApiHandler {
	
	public static Logger logger = LoggerFactory.getLogger(ApiHandler.class);

	private final static Pattern METHODNAME_PATTERN = Pattern.compile("^\\$\\{(\\w+)\\((.*?)\\)\\}$");

    private final static Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(\\w*\\[*\\d*\\]*\\.*)\\}");

    private final static Pattern JSONARRAY_PATTERN = Pattern.compile("^\\[.*?\\]$");
    
    private final static Pattern JSONOBJECT_PATTERN = Pattern.compile("^\\{.*?\\}$");

    private final static Pattern DB_PATTERN = Pattern.compile("\\$\\{(\\w+DBS)\\}");

    private final static Pattern NUM_PATTERN = Pattern.compile("(\"number\\[(\\d*\\.?\\d*)\\]\")");

    private final static Pattern LIST_PATTERN = Pattern.compile("(\"list\\[(\\[.*\\])\\]\")");
    
    private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
    
    private final static Pattern CRLF_PATTERN = Pattern.compile("(\r\n|\r|\n|\n\r|\t)");
    
    @Autowired
    WebSocket webSocket;
    
    /**
     * 替换字符串中的换行、tab
     * @param str
     * @return
     */
    public String removeCRLF(String str){
    	Matcher matcher = CRLF_PATTERN.matcher(str);
    	if(matcher.find()){
    		return matcher.replaceAll("");
    	}else{
    		return str;
    	}
    }
    
    /**
     * 判断字符串中是否包含要替换变量
     * @param variable
     * @return
     */
    public boolean matchVariable(String variable){
    	Matcher matcher = VARIABLE_PATTERN.matcher(variable);
    	return matcher.matches();
    }
    
    /**
     * 判断字符串中是否包含要替换方法
     * @param method
     * @return
     */
    public boolean matchMethod(String method){
    	Matcher matcher = METHODNAME_PATTERN.matcher(method);
    	return matcher.matches();
    }
    
    public void compareStr(String key, String expectedStr, String actualStr, Map<String, Object> map, List<String> list){
		if(SAVE_PATTERN.matcher(expectedStr).matches()){
			Matcher matcher = SAVE_PATTERN.matcher(expectedStr);
			if(matcher.find()){
				map.put(matcher.group(1), actualStr);
				logger.info("true: 已保存变量" + matcher.group(1) + " ，变量值为：" + actualStr);
				
				list.add("true: 已保存变量" + matcher.group(1) + " ，变量值为：" + actualStr);
				webSocket.sendMsgTo("###已保存变量" + matcher.group(1) + " ，变量值为：" + actualStr, "123");
			}
		}else{
			if(!expectedStr.equals(actualStr)){
				logger.warn("false: " + key + " 预期结果是：" + expectedStr + ",但实际结果是：" + actualStr);
				
				list.add("false: " + key + " 预期结果是：" + expectedStr + ",但实际结果是：" + actualStr);
				webSocket.sendMsgTo("###" + key + " 预期结果是：" + expectedStr + ",但实际结果是：" + actualStr, "123");
			}else{
				logger.info("true: " + key + " 预期结果、实际结果相同，结果为：" + expectedStr);
				
				list.add("true: " + key + " 预期结果、实际结果相同，结果为：" + expectedStr);
				webSocket.sendMsgTo("###" + key + " 预期结果、实际结果相同，结果为：" + expectedStr, "123");
			}
		}
	}
	
	public boolean compareJsonObj(JSONObject expectedJsonObj, JSONObject actualJsonObj, Map<String, Object> map, List<String> list){
		Set<String> keys = expectedJsonObj.keySet();
		
		boolean result = true;
		
		for(String key : keys){
			Object expectedValue = expectedJsonObj.get(key);
			Object actualValue = actualJsonObj.get(key);
			
			if(expectedValue instanceof JSONObject){
				if(actualValue instanceof JSONObject){
					compareJsonObj((JSONObject)expectedValue, (JSONObject)actualValue, map, list);
				}else{
					logger.warn("false: " + key + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
					list.add("false: " + key + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
					webSocket.sendMsgTo("###" + key + "的预期值：" + expectedValue + ",但实际值为：" + actualValue, "123");
					result = false;
				}
			}else if(expectedValue instanceof JSONArray){
				logger.warn("false: 预期结果暂不支持数组, " + key + " : " + expectedValue);
				list.add("false: 预期结果暂不支持数组, " + key + " : " + expectedValue);
				webSocket.sendMsgTo("###预期结果暂不支持数组, " + key + " : " + expectedValue, "123");
				result = false;
			}else{
				compareStr(key, expectedJsonObj.getString(key), actualJsonObj.getString(key), map, list);
			}
		}
		
		/**
		keys.forEach(x->{
			Object expectedValue = expectedJsonObj.get(x);
			Object actualValue = actualJsonObj.get(x);
			
			if(expectedValue instanceof JSONObject){
				if(actualValue instanceof JSONObject){
					compareJsonObj((JSONObject)expectedValue, (JSONObject)actualValue, map, list);
				}else{
					logger.warn("false: " + x + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
					list.add("false: " + x + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
				}
			}else if(expectedValue instanceof JSONArray){
				logger.warn("false: 预期结果暂不支持数组, " + x + " : " + expectedValue);
				list.add("false: 预期结果暂不支持数组, " + x + " : " + expectedValue);
			}else{
				compareStr(x, expectedJsonObj.getString(x), actualJsonObj.getString(x), map, list);
			}
		});
		**/
		
		return result;
	}
    
	/**
	 * 替换变量
	 * @param map
	 * @param variable
	 * @return
	 */
    public Map<Boolean, String> handleVariable(Map<String, Object> map, String variable){
    	Map<Boolean, String> resultMap = new HashMap<Boolean, String>();
    	
    	Matcher matcher = VARIABLE_PATTERN.matcher(variable);
    	String allValue = null;
    	
    	while(matcher.find()){
    		int count = matcher.groupCount();
    		
    		if(count == 1){
    			String key = matcher.group(1);
    			logger.info("开始替换变量" + key);
    			
    			Object value = map.get(key);
    			
    			if(null == value){
        			logger.error("变量" + key + "不在上下文中");
        			
        			resultMap.put(false, "变量" + key + "不在上下文中");
        			break;
        		}else{
        			String replaceValue = String.valueOf(value);
        			logger.info("变量" + key + "值为：" + replaceValue);
        			
        			if(null == allValue){
        				allValue = variable.replaceAll("\\$\\{" + key + "\\}", replaceValue);
        			}else{
        				allValue = allValue.replaceAll("\\$\\{" + key + "\\}", replaceValue);
        			}
        		}
    		}
    	}
    	
    	if(!resultMap.containsKey(false) && allValue != null){
    		resultMap.put(true, allValue);
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
    	List<String> listArgs = null;
    	String [] array = null;
    	
    	while(matcher.find()){
    		funcName = matcher.group(1);
    		
    		if(matcher.groupCount() == 2){
    			args = matcher.group(2);
    		}
    		
    		logger.info("开始替换函数" + funcName + " ,参数为：" + args);
    	}
    	
    	if(null != args){
    		listArgs = new ArrayList<String>();
    				
    		Pattern  pattern = Pattern.compile("(\".+?\")");
        	
        	matcher = pattern.matcher(args);
        	
        	while(matcher.find()){
        		String arg = matcher.group(1);
        		Pattern p2 = Pattern.compile("\"(.+?)\"");
            	Matcher  m2 = p2.matcher(arg);
            	if(m2.find()){
            		listArgs.add(m2.group(1)); 
            	}else{
            		listArgs.add(matcher.group(1)); 
            	}
        	}
        	
        	array = listArgs.toArray(new String[listArgs.size()]);
    	}
    	
    	String result = null;
    	
		try {
			if(null == args){
				result = InvokeUtils.invokeMethod(funcName);
			}else{
				result = InvokeUtils.invokeMethod(funcName, array);
			}
			
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
    
    public boolean handleResponseBody(Map<String, Object> map, String expectedResponseBody, String actualResponseBody, String responseBodyType, List<String> list){
    	logger.info("开始处理响应消息");
    	
    	boolean result = true;
    	
    	if("1".equals(responseBodyType)){//text响应消息
    		Matcher matcher = SAVE_PATTERN.matcher(expectedResponseBody);
    		
    		if(matcher.matches()){
    			if(matcher.find()){
    				map.put(matcher.group(1), actualResponseBody);
    			}
    			
    			logger.info("true: 已保存变量：" + matcher.group(1) + " ，变量值为：" + actualResponseBody);
    			
    			list.add("true: 已保存变量：" + matcher.group(1) + " ，变量值为：" + actualResponseBody);
    			webSocket.sendMsgTo("###已保存变量：" + matcher.group(1) + " ，变量值为：" + actualResponseBody, "123");
    			return result;
    		}
    		
    		if(expectedResponseBody.equals(actualResponseBody)){
    			logger.info("true: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
    			
    			list.add("true: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
    			webSocket.sendMsgTo("###预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody, "123");
    		}else{
    			logger.info("false: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
    			list.add("false: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
    			webSocket.sendMsgTo("###预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody, "123");
    			result = false;
    		}
    		
    		return result;
    	}
    	
    	//json响应消息
    	if(JSONOBJECT_PATTERN.matcher(expectedResponseBody).matches()){
    		if(JSONOBJECT_PATTERN.matcher(actualResponseBody).matches()){
    			result = compareJsonObj(JSONObject.parseObject(expectedResponseBody), JSONObject.parseObject(actualResponseBody), map, list);
    		}else{
    			logger.error("响应消息不是json格式");
    			list.add("响应消息不是json格式");
    			webSocket.sendMsgTo("响应消息不是json格式", "123");
    			result = false;
    		}
    	}
    	
    	return result;
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
        }**/
    	
    	/**
        String vv = "(\"1\", \"2\")";
        Pattern pattern = Pattern.compile("(\".+?\")");
        
        Matcher  matcher = pattern.matcher(vv);
        
        while(matcher.find()){
        	String var = matcher.group(1);
        	Pattern p2 = Pattern.compile("\"(.+?)\"");
        	Matcher  m2 = p2.matcher(var);
        	while(m2.find()){
        		System.out.println(m2.group(1));
        	}
        }**/
    	
    	/**
    	String str = "ddd${generateRandomValue(\"${nnn}\", \"${mmm}\")}xxxx${xxx}yyyy${zz}";
    	Pattern var = Pattern.compile("\\$\\{(\\w*\\[*\\d*\\]*\\.*)\\}");
    	Pattern method = Pattern.compile("^\\$\\{(\\w+)\\((.*?)\\)\\}$");
    	Matcher matcher = var.matcher(str);
    	String replaceStr = null;
    	while(matcher.find()){
    		System.out.println(matcher.group(1));
    		
    		if(null == replaceStr){
    			replaceStr = str.replaceAll("\\$\\{" + matcher.group(1) + "\\}", "99");
    		}else{
    			replaceStr = replaceStr.replaceAll("\\$\\{" + matcher.group(1) + "\\}", "99");
    		}
    		
    		System.out.println(replaceStr);
    	}**/
    	
    	
    	/**
    	String jsonStr = "{\"a\":1, \"b\":[{\"b1\":1}], \"c\":{\"d\":1}}";
    	System.out.println("");**/
    	
    	String str = "xxx\r\nyyyy\r\tzzzz";
    	
    	ApiHandler vh = new ApiHandler();
    	System.out.println(vh.removeCRLF(str));
    }
}
