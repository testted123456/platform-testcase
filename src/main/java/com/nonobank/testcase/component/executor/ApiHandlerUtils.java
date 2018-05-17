package com.nonobank.testcase.component.executor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.DBCfg;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.service.DBCfgService;
import com.nonobank.testcase.service.EnvService;
import com.nonobank.testcase.utils.dll.InvokeUtils;

@Component
public class ApiHandlerUtils {

	public static Logger logger = LoggerFactory.getLogger(ApiHandlerUtils.class);

	private final static Pattern SINGLE_VARIABLE_PATTERN = 
//			Pattern.compile("\\$\\{(\\w+.*)\\}");
			Pattern.compile("\\$\\{(\\w*\\[*\\d*\\]*\\.*)\\}");
	
	private final static Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(\\w+.*)\\}");
	
	private final static Pattern JSONARRAY_PATTERN =   Pattern.compile("\\$\\{(\\w+)\\[(\\d+)\\]\\}");
	
	private final static Pattern JSONOBJECT_PATTERN =   
//			Pattern.compile("\\$\\{[\\w|\\[|\\]]+\\.[\\w|\\[|\\]]+\\}");
			Pattern.compile("\\$\\{([\\w|\\[|\\]|\\d]+?\\.[\\w|\\[|\\]\\d\\.]+?)\\}");

	//方法函数
	private final static Pattern METHODNAME_PATTERN = 
			Pattern.compile("^\\$\\{(\\w+)\\((.*?)\\)\\}$");
//	 Pattern.compile("\\$\\{(\\w+)\\(.*?\\)\\}");

	//换行
	private final static Pattern CRLF_PATTERN = Pattern.compile("(\r\n|\r|\n|\n\r|\t|\\\\n)");
	
	//回车
	private final static Pattern ENTER_PATTERN = Pattern.compile("\\\\\\\\n");
	
	//保存变量
	private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
	
	//json中value视为对象，需去掉引号
	private final static Pattern QUOTE_PATTERN = Pattern.compile("\\#\\{(\\w*\\d*\\.*)\\}");
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	EnvService envService;
	
	@Autowired
	DBCfgService dbCfgService;

	public void compareStr(String key, String expectedStr, String actualStr, Map<String, Object> map, 
			Map<String, String> handledResult, 
			String sessionId, Map<String, Boolean> resultOfMap) {
		if(null == expectedStr ){
			if(null == actualStr){
				webSocket.sendVar("**" + key + "** 预期结果、实际结果相同，结果为：" + expectedStr, sessionId);
			}else{
				webSocket.sendVar("**" + key + "** 预期值：" + expectedStr + "，实际值：" + actualStr, sessionId);
				resultOfMap.put("result", false);
			}
			
			return;
		}
		
		if (SAVE_PATTERN.matcher(expectedStr).matches()) {//保存变量
			Matcher matcher = SAVE_PATTERN.matcher(expectedStr);
			if (matcher.find()) {
				map.put(matcher.group(1), actualStr);
				logger.info("true: 已保存变量" + matcher.group(1) + " ，变量值为：" + actualStr);
				webSocket.sendVar("**" + matcher.group(1) + "**" + " 已保存" + " ，变量值为：" + actualStr, sessionId);
				handledResult.put(matcher.group(1),  "已保存" + " ，变量值为：" + actualStr);
			}
		} else {
			if (!expectedStr.equals(actualStr)) {
				logger.warn("false: " + key + " 预期结果是：" + expectedStr + ",但实际结果是：" + actualStr);
				resultOfMap.put("result", false);
				webSocket.sendVar("**" + key + "** 预期值：" + expectedStr + "，实际值：" + actualStr, sessionId);
				handledResult.put(key, "预期值：" + expectedStr + "，实际值：" + actualStr);
			} else {
				logger.info("true: " + key + " 预期结果、实际结果相同，结果为：" + expectedStr);
				webSocket.sendVar("**" + key + "** 预期结果、实际结果相同，结果为：" + expectedStr, sessionId);
				handledResult.put(key, "预期结果、实际结果相同，结果为：" + expectedStr);
			}
		}
	}

	public void compareJsonObj(JSONObject expectedJsonObj, JSONObject actualJsonObj, Map<String, Object> map,
			Map<String, String> handledResult,
			String sessionId, Map<String, Boolean> resultOfMap) {
		Set<String> keys = expectedJsonObj.keySet();

//		boolean result = true;

		for (String key : keys) {
			Object expectedValue = expectedJsonObj.get(key);
			Object actualValue = actualJsonObj.get(key);

			if (expectedValue instanceof JSONObject) {
				if (actualValue instanceof JSONObject) {
					compareJsonObj((JSONObject) expectedValue, (JSONObject) actualValue, map, 
							handledResult, 
							sessionId, resultOfMap);
				} else {
					logger.warn("false: " + key + "的预期值：" + expectedValue + ",但实际值为：" + actualValue);
//					webSocket.sendMsgTo("###" + key + "的预期值：" + expectedValue + ",但实际值为：" + actualValue, "123");
					webSocket.sendVar("**" + key + "** 预期值：" + expectedValue + "，实际值：" + actualValue, sessionId);
					handledResult.put(key, "预期值：" + expectedValue + "，实际值：" + actualValue);
					resultOfMap.put("result", false);
				}
			} else if (expectedValue instanceof JSONArray) {
				logger.warn("false: 预期结果暂不支持数组, " + key + " : " + expectedValue);
				webSocket.sendVar("预期结果暂不支持数组，" + key + " : " + expectedValue, sessionId);
				handledResult.put(key, "预期结果暂不支持数组，" + expectedValue);
				resultOfMap.put("result", false);
			} else {
				compareStr(key, expectedJsonObj.getString(key), actualJsonObj.getString(key), map, 
						handledResult, 
						sessionId, resultOfMap);
			}
		}
	}

	/**
	 * 判断字符串中是否包含要替换变量
	 * 
	 * @param variable
	 * @return
	 */
	public boolean variableMatched(String variable) {
		Matcher matcher = SINGLE_VARIABLE_PATTERN.matcher(variable);
		return matcher.find();
	}

	/**
	 * 判断字符串中是否包含要替换方法
	 * 
	 * @param method
	 * @return
	 */
	public boolean methodMatched(String method) {
		Matcher matcher = METHODNAME_PATTERN.matcher(method);
		return matcher.find();
	}

	/**
	 * 替换字符串中的换行、tab
	 * 
	 * @param str
	 * @return
	 */
	public static String removeCRLF(String str) {
		System.out.println(str);
		Matcher matcher = CRLF_PATTERN.matcher(str);
		if (matcher.find()) {
			return matcher.replaceAll("");
		} else {
			return str;
		}
	}
	
	public static String removeEnter(String str){
		Matcher matcher = ENTER_PATTERN.matcher(str);
		if (matcher.find()) {
			return matcher.replaceAll("");
		} else {
			return str;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	public Map<Boolean, String> handleQuoteVariable(String variable, Map<String, Object> map){
		Map<Boolean, String> resultMap = new HashMap<Boolean, String>();
		Matcher matcher = QUOTE_PATTERN.matcher(variable);
		String allValue = null;
		
		while(matcher.find()){
			String key = matcher.group(1);
			logger.info("开始去除变量{}的引号", key);
			Object value = map.get(key);
			
			if (null == value) {
				logger.error("变量" + key + "不在上下文中");
				resultMap.put(false, "变量" + key + "不在上下文中");
				break;
			} else {
				String replaceValue = String.valueOf(value);
				logger.info("变量" + key + "值为：" + replaceValue);

				if (null == allValue) {
					allValue = variable.replaceAll("\"#\\{" + key + "\\}\"", replaceValue);
				} else {
					allValue = allValue.replaceAll("\"#\\{" + key + "\\}\"", replaceValue);
				}
			}
			
			if (!resultMap.containsKey(false) && allValue != null) {
				resultMap.put(true, allValue);
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 处理非对象、数组的变量
	 * @param variable
	 * @param map
	 * @return
	 */
	public Map<Boolean, String> handleSingleVariable(String variable, Map<String, Object> map){

		Map<Boolean, String> resultMap = new HashMap<Boolean, String>();

		Matcher matcher = SINGLE_VARIABLE_PATTERN.matcher(variable);
		String allValue = null;

		while (matcher.find()) {
//			int count = matcher.groupCount();

//			if (count == 1) {
				String key = matcher.group(1);
				logger.info("开始替换变量" + key);
				Object value = map.get(key);

				if (null == value) {
					logger.error("变量" + key + "不在上下文中");
					resultMap.put(false, "变量" + key + "不在上下文中");
					break;
				} else {
					String replaceValue = String.valueOf(value);
					logger.info("变量" + key + "值为：" + replaceValue);

					if (null == allValue) {
						allValue = variable.replaceAll("\\$\\{" + key + "\\}", replaceValue);
					} else {
						allValue = allValue.replaceAll("\\$\\{" + key + "\\}", replaceValue);
					}
				}
//			}
		}

		if (!resultMap.containsKey(false) && allValue != null) {
			resultMap.put(true, allValue);
		}

		return resultMap;
	}
	
	/**
	 * 替换api中的自定义变量
	 * 
	 * @param map
	 * @param variable
	 * @return
	 */
	public Map<Boolean, String> handleVariable(Map<String, Object> map, String variable) {
		Map<Boolean, String> resultMap = new HashMap<Boolean, String>();
		
//		Matcher m = VARIABLE_PATTERN.matcher(variable);
//		
//		if(m.find()){
//			String key = m.group(1);
			
			if(JSONARRAY_PATTERN.matcher(variable).find()){
				resultMap = handleArrayVariable(variable, map);
			}else if(JSONOBJECT_PATTERN.matcher(variable).find()){
				resultMap = handleObject(variable, map);
			}else {
				if(VARIABLE_PATTERN.matcher(variable).find()){
					resultMap = handleSingleVariable(variable, map);
				}
				if(QUOTE_PATTERN.matcher(variable).find()){
					resultMap = handleQuoteVariable(variable, map);
				}
			}
				
			
//		}
		
		return resultMap;
			
//		Map<Boolean, String> resultMap = new HashMap<Boolean, String>();
//
//		Matcher matcher = VARIABLE_PATTERN.matcher(variable);
//		String allValue = null;
//
//		while (matcher.find()) {
////			int count = matcher.groupCount();
//
////			if (count == 1) {
//				String key = matcher.group(1);
//				logger.info("开始替换变量" + key);
//				Object value = null;
//				
//				if(JSONARRAY_PATTERN.matcher(key).find()){
//					try{
//						value = handleArrayVariable(key, map);
//					}catch(Exception e){
//						logger.error("替换变量{" + key + "}失败，" + e.getLocalizedMessage());
//						value = null;
//					}
//				}else if(JSONOBJECT_PATTERN.matcher(key).find()){
//					try{
//						value = handleObject(variable, map);
//					}catch(Exception e){
//						logger.error("替换变量{" + key + "}失败，" + e.getLocalizedMessage());
//						value = null;
//					}
//				}else{
//					value = map.get(key);
//				}
//
//				if (null == value) {
//					logger.error("变量" + key + "不在上下文中");
//					resultMap.put(false, "变量" + key + "不在上下文中");
//					break;
//				} else {
//					String replaceValue = String.valueOf(value);
//					logger.info("变量" + key + "值为：" + replaceValue);
//
//					if (null == allValue) {
//						allValue = variable.replaceAll("\\$\\{" + key + "\\}", replaceValue);
//					} else {
//						allValue = allValue.replaceAll("\\$\\{" + key + "\\}", replaceValue);
//					}
//				}
////			}
//		}
//
//		if (!resultMap.containsKey(false) && allValue != null) {
//			resultMap.put(true, allValue);
//		}
//
//		return resultMap;
	}

	/**
	 * 替换方法
	 * 
	 * @param method
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Map<Boolean, String> handleMethod(String method, String env) {
		Map<Boolean, String> map = new HashMap<Boolean, String>();
		Matcher matcher = METHODNAME_PATTERN.matcher(method);

		String funcName = null;
		String args = null;
		List<String> listArgs = null;
		String[] array = null;

		while (matcher.find()) {
			funcName = matcher.group(1);

			if (matcher.groupCount() == 2) {
				args = matcher.group(2);
			}

			logger.info("开始替换函数" + funcName + " ,参数为：" + args);
		}
		
		if (null != args) {
			listArgs = new ArrayList<String>();
			Pattern pattern = Pattern.compile("(\".+?\")");
			matcher = pattern.matcher(args);

			while (matcher.find()) {
				String arg = matcher.group(1);
				Pattern p2 = Pattern.compile("\"(.+?)\"");
				Matcher m2 = p2.matcher(arg);
				if (m2.find()) {
					listArgs.add(m2.group(1));
				} else {
					listArgs.add(matcher.group(1));
				}
			}

			array = listArgs.toArray(new String[listArgs.size()]);
		}

		String result = null;

		try {
			if (null == args) {
				result = InvokeUtils.invokeMethod(funcName);
			} else {
				if(funcName.endsWith("_db")){//操作数据库函数
					funcName = funcName.substring(0, funcName.length()-3);
					Env envEntity = envService.findByName(env);
					Integer dbGroupId = envEntity.getDbGroup().getId();
					
					String sql = null;
					
					if(array.length > 0){//函数有参数，第一个参数为待执行sql
						 sql = array[0];
					}
					
					//如果没有指定数据库分组，则分组名默认为default
					String dbGroupName = "default";
					
					if(array.length == 2){
						dbGroupName = array[1];		
					}

					DBCfg dbCfg = dbCfgService.findByDbGroupIdAndName(dbGroupId, dbGroupName);
					
					if(null == dbCfg){
						throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), 
								"数据库分组<" + envEntity.getDbGroup().getGroupName() +
								">, 名称<" + dbGroupName + ">没有配置地址");
					}
					
					String mySql_driver = "com.mysql.jdbc.Driver";
					String db_name = dbCfg.getDbName();
					String mySql_url = "jdbc:mysql://" + dbCfg.getIp() + ":" + "3306" + "/" + db_name;
					String user_name = dbCfg.getUserName();
					String db_password = dbCfg.getPassword();
					
					String [] funcArray = null;
					
					if(null != sql){
						funcArray = new String[5];
						funcArray[4] = sql;
					}else{
						funcArray = new String[4];
					}
					
					funcArray[0] = mySql_driver;
					funcArray[1] = mySql_url;
					funcArray[2] = user_name;
					funcArray[3] = db_password;
					
					array = funcArray;
				}
				
				result = InvokeUtils.invokeMethod(funcName, array);
			}

			logger.info("函数" + funcName + "执行结果为：" + result);
			map.put(true, result);
		} catch (Exception e) {
			String errMsg = ExceptionUtils.getStackTrace(e);
			logger.error("执行方法{" + method + "}抛异常:" + errMsg );
			map.put(false,  e.getLocalizedMessage());
//			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 处理含有.的变量，当成JSONObject
	 * @param variable
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<Boolean, String> handleObject(String variable, Map<String, Object> map){
		Map<Boolean, String> resultMap = new HashMap<>();
		Map<Boolean, String> subResultMap = new HashMap<>();
		Object object = null;
		String replacedValue = null;
		
		try{
			Matcher m = JSONOBJECT_PATTERN.matcher(variable);
			
			while(m.find()){
				String subVariable = m.group(1);
				String [] varArray = subVariable.split("\\.");
				String var = varArray[0];
				
				if(var.matches("\\w*\\d*\\[\\d\\]")){
					subResultMap = handleArrayVariable("${" + var + "}", map);
				}else{
					subResultMap = handleSingleVariable("${" + var + "}", map);
				}
				
				if(subResultMap.size() == 0){
					object = map.get(var);
				}else if(subResultMap.containsKey(true)){
					object = subResultMap.get(true);
				}else  if(subResultMap.containsKey(false)){
					resultMap.put(false, subResultMap.get(false));
					break;
				}
				
				int size = varArray.length;
				
				for(int i=1;i<size;i++){
					var = varArray[i];
					map = JSONObject.parseObject(String.valueOf(object), Map.class);
//					subResultMap =  handleArrayVariable(var, map);
					
					if(var.matches("\\w*\\d*\\[\\d\\]")){
						subResultMap = handleArrayVariable("${" + var + "}", map);
					}else{
						subResultMap = handleSingleVariable("${" + var + "}", map);
					}
					
					if(subResultMap.size() == 0){
						object = map.get(var);
					}else if(subResultMap.containsKey(true)){
						object = subResultMap.get(true);
					}else  if(subResultMap.containsKey(false)){
						resultMap.put(false, subResultMap.get(false));
						break;
					}
				}
				
				if(resultMap.containsKey(false)){
					break;
				}else if(object != null){
					if (null == replacedValue) {
						replacedValue = variable.replaceAll("\\$\\{" + subVariable + "\\}", String.valueOf(object));
					} else {
						replacedValue = replacedValue.replaceAll("\\$\\{" + subVariable + "\\}", String.valueOf(object));
					}
				}
				
			}
			
		}catch(Exception e){
			logger.error("替换变量{" + variable + "}失败，" + e.getLocalizedMessage());
			resultMap.put(false, e.getLocalizedMessage());
		}
		
		if(!resultMap.containsKey(false) && null != object){
			resultMap.put(true, String.valueOf(object));
		}
		
		return resultMap;
	}
	
	/**
	 * 处理数组变量
	 * @param variable
	 * @param map
	 * @return
	 */
	public Map<Boolean, String> handleArrayVariable(String variable, Map<String, Object> map){
		Map<Boolean, String> resultMap = new HashMap<>();
		Object object = null;
		String replacedValue = null;
		
		try{
			Matcher m = JSONARRAY_PATTERN.matcher(variable);
			
			while(m.find()){
				String key = m.group(1);
				String index = m.group(2);
				object = map.get(key);
				JSONArray jsonArr = JSONArray.parseArray(String.valueOf(object));
				String value = jsonArr.getString(Integer.valueOf(index));
				
				if(null == value){
					resultMap.put(false, "变量{" + variable + "}可能不在上下文中");
					break;
				}else{
					if (null == replacedValue) {
						replacedValue = variable.replaceAll("\\$\\{" + key + "\\[" + index + "\\]" + "\\}", value);
					} else {
						replacedValue = replacedValue.replaceAll("\\$\\{" + key + "\\[" + index + "\\]" + "\\}", value);
					}
				}
			}
//			else{
//				object = map.get(variable);
//			}
		}catch(Exception e){
			logger.error("替换变量{" + variable + "}失败，" + e.getLocalizedMessage());
			resultMap.put(false, e.getLocalizedMessage());
			return resultMap;
		}
		
		if (!resultMap.containsKey(false) && replacedValue != null) {
			resultMap.put(true, replacedValue);
		}
		
		return resultMap;
	}
	
	public static void main(String [] args){
		ApiHandlerUtils tt = new ApiHandlerUtils();
		String str = "{\"a\":\"#{abc}\"}";
		Matcher m = QUOTE_PATTERN.matcher(str);
		while(m.find()){
			System.out.println(m.group(1));
			str = str.replaceAll("\"#\\{abc\\}\"", "123");
			System.out.println(str);
		}
//		String str2 = "abc[1]1";
//		
//		String str = "${abc.a[0].b}";
//		String str1 = "xyz${def[1]}abc${def[0]}";
//		
//		Map<String, Object> map = new HashMap<>();
//		map.put("abc", "{\"a\":[{\"b\":2}, {\"c\":3}]}");
//		map.put("def", "[1,2]");
//		
//		Matcher m = 
////				JSONARRAY_PATTERN.matcher(str1);
//				JSONOBJECT_PATTERN.matcher(str);
//		
//		Map<Boolean, String> resMap = 
//				tt.handleObject(str, map);
////				handleArrayVariable(str1, map);
//		System.out.println(resMap.get(true));
//		System.out.println(resMap.get(false));
	}
}
