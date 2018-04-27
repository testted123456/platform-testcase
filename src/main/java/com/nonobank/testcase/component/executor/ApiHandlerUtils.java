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
import com.nonobank.testcase.entity.DBCfg;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.service.DBCfgService;
import com.nonobank.testcase.service.EnvService;
import com.nonobank.testcase.utils.dll.InvokeUtils;

@Component
public class ApiHandlerUtils {

	public static Logger logger = LoggerFactory.getLogger(ApiHandlerUtils.class);

	private final static Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(\\w*\\[*\\d*\\]*\\.*)\\}");

	private final static Pattern METHODNAME_PATTERN = 
			Pattern.compile("^\\$\\{(\\w+)\\((.*?)\\)\\}$");
//	 Pattern.compile("\\$\\{(\\w+)\\(.*?\\)\\}");

	private final static Pattern CRLF_PATTERN = Pattern.compile("(\r\n|\r|\n|\n\r|\t|\\\\n)");
	
	private final static Pattern ENTER_PATTERN = Pattern.compile("\\\\\\\\n");
	
	private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
	
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
		
		if (SAVE_PATTERN.matcher(expectedStr).matches()) {
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
		Matcher matcher = VARIABLE_PATTERN.matcher(variable);
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
	 * 替换api中的自定义变量
	 * 
	 * @param map
	 * @param variable
	 * @return
	 */
	public Map<Boolean, String> handleVariable(Map<String, Object> map, String variable) {
		Map<Boolean, String> resultMap = new HashMap<Boolean, String>();

		Matcher matcher = VARIABLE_PATTERN.matcher(variable);
		String allValue = null;

		while (matcher.find()) {
			int count = matcher.groupCount();

			if (count == 1) {
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
			}
		}

		if (!resultMap.containsKey(false) && allValue != null) {
			resultMap.put(true, allValue);
		}

		return resultMap;
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
			logger.error("执行方法{}抛异常", method);
			map.put(false, "执行方法" + method + "抛异常");
			e.printStackTrace();
		}

		return map;
	}
	
	public static void main(String [] args){
		String str = "${getMultField_db(\"SELECT id,userid FROM debtsale WHERE status =3 and optype IN (1,3) AND debtsale.title='缺钱' ORDER BY id DESC LIMIT 1\")}";
		
//	  Pattern cp = Pattern.compile("(\\\\n)");
//	  System.out.println(cp.matcher(str).find());
		
		System.out.println(Pattern.compile("\\$\\{(\\w+)\\(.*?\\)\\}").matcher(str).find());
	  System.out.println(METHODNAME_PATTERN.matcher(str).find());

	}
}
