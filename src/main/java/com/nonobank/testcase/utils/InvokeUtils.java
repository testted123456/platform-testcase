package com.nonobank.testcase.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.dataProvider.annotation.Info;
import com.nonobank.testcase.dataProvider.annotation.Param;
import com.nonobank.testcase.dataProvider.annotation.Return;

public class InvokeUtils {

    private final static Pattern methodName_patter = Pattern.compile("\\$\\{(\\w+)\\(.*\\)");

    private final static Pattern fullMethod_patter = Pattern.compile("\\$\\{(.*)\\}");

    private final static Pattern className_patter = Pattern.compile("\\.(\\w+)$");

    private final static Pattern parameter_patter = Pattern.compile("(\\$\\{.*)_db\\((.*)");


    private final static String[] envkeys = {"mySql_driver",
            "mySql_url_nono",
            "db_name_nono",
            "db_password_nono"};

    public static String createInvokeMethod(String value,Map<String,String> configMap){
        String classMethod = appendClassName(value);
        Matcher matcher = parameter_patter.matcher(classMethod);
        if (matcher.find()){
            String parameter = "";
            if(matcher.group(2).length()>2){
                parameter = "("+createParameter(configMap)+",";

            }else{
                parameter = "("+createParameter(configMap);
            }
            return matcher.group(1)+parameter+matcher.group(2);
        }else{
            return classMethod;
        }
    }


    public static String createParameter(Map<String,String> config){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<envkeys.length;i++){
            stringBuffer.append("\"");
            stringBuffer.append(config.get(envkeys[i]));
            stringBuffer.append("\"");
            if(!(i==envkeys.length-1)){
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }


    public static String appendClassName(String value) {
        value = value != null ? value : "";
        Matcher nameMatcher = methodName_patter.matcher(value);
        if (nameMatcher.find()) {
            String fullclassName = InvokeUtils.getClassName(nameMatcher.group(1));
            Matcher fullMatcher = fullMethod_patter.matcher(value);
            String fullMethodName = fullMatcher.find() ? fullMatcher.group(1) : "";

            Matcher classMatcher = className_patter.matcher(fullclassName);
            String className = classMatcher.find() ? classMatcher.group(1) : "";

            return "${" + className + "." + fullMethodName + "}";

        }
        return value;
    }

    public static List<JSONObject> getMethods() {
        List<JSONObject> listOfJson = new LinkedList<JSONObject>();

        List<Class<?>> list = DataProviderUtil.getClasses("com.nonobank.testCase.dataProvider.common");

        for (Class<?> clazz : list) {
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                Info info = method.getAnnotation(Info.class);
                Param param = method.getAnnotation(Param.class);
                Return ret = method.getAnnotation(Return.class);

                if (null != info && null != param) {
                    String methodName = info.name();
                    String returnMsg = info.desc();
                    String[] paramsName = param.name();
                    String[] paramsDesc = param.desc();
                    int lengthOfParamsName = paramsName.length;
                    int lengthOfParamsDesc = paramsDesc.length;
                    int length = lengthOfParamsName > lengthOfParamsDesc ? lengthOfParamsDesc : lengthOfParamsName;
                    String joinedParamsDesc = null;

                    for (int i = 0; i < length; i++) {
                        int j = i + 1;
                        if(null != joinedParamsDesc){
                        	joinedParamsDesc = joinedParamsDesc + "参数" + j + "(" + paramsName[i] + "):" + paramsDesc[i] + ";";
                        }else{
                        	joinedParamsDesc = "参数" + j + "(" + paramsName[i] + "):" + paramsDesc[i] + ";";
                        }
                    }

                    JSONObject obj = new JSONObject();
                    obj.put("func", methodName);
                    
                    if(returnMsg != null){
                    	if(joinedParamsDesc==null){
                    		obj.put("desc", "方法返回：" + returnMsg + ";");
                    	}else{
                    		obj.put("desc", "方法返回：" + returnMsg + ";" + joinedParamsDesc);
                    	}
                    }else{
                    	obj.put("desc", joinedParamsDesc==null?"":joinedParamsDesc);
                    }
                    
                    listOfJson.add(obj);
                }

            }
        }

        return listOfJson;
    }
    
    public static String invokeMethod(String methodName, String [] args) throws InstantiationException, 
    IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException{
    	
    	int len = args.length;
    	Class<?> [] clazzes = new Class[len];
    	
    	for(int i=0;i<len;i++){
    		clazzes[i] = String.class;
    	}
    	
    	Class<?> clazz = getClass(methodName);
		Object object = clazz.newInstance();
		Method method = clazz.getDeclaredMethod(methodName, clazzes);
		Object result = 
				method.invoke(object, clazzes);
		return result.toString();
    }


    private static String joinMethod(Info mInfo, Param mParam) {
        String methodName = "";
        String[] paramNames = new String[0];
        if (mInfo != null) {
            methodName = mInfo.name();
        }
        String methodDesc = methodName + "(";
        if (mParam != null) {
            paramNames = mParam.name();
            if (paramNames != null && paramNames.length > 0) {
                for (int i = 0; i < paramNames.length; ++i) {
                    if (i == paramNames.length - 1) {
                        //最后一个元素
                        methodDesc += paramNames[i] + ")";
                        break;
                    }
                    methodDesc += paramNames[i] + ",";
                }
            } else {
                methodDesc = methodName + "()";
            }
        }
        return methodDesc;
    }
    
    public static Class getClass(String methodName){
    	 List<Class<?>> claszes = DataProviderUtil.getClasses("com.nonobank.testCase.dataProvider");
         
    	 for (Class claz : claszes) {
             Method[] methods = claz.getDeclaredMethods();
             
             for (Method method : methods) {
                 String methodNameFromPack = method.getName();
                 
                 if (methodNameFromPack != null && methodNameFromPack.trim().equals(methodName.trim())) {
                     return claz;
                 }
             }
         }
         
         return null;
    }

    //根据方法名返回方法所属类名
    private static String getClassName(String methodName) {
        List<Class<?>> claszes = DataProviderUtil.getClasses("com.nonobank.testCase.dataProvider");
        for (Class claz : claszes) {
            Method[] methods = claz.getDeclaredMethods();
            for (Method method : methods) {
                String methodNameFromPack = method.getName();
                if (methodNameFromPack != null && methodNameFromPack.trim().equals(methodName.trim())) {
                    return claz.getName();
                }
            }
        }
        
        return "";
    }

}
