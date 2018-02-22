package com.nonobank.testcase.dataProvider.common;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.nonobank.testcase.dataProvider.annotation.Info;
import com.nonobank.testcase.dataProvider.annotation.Param;
import com.nonobank.testcase.dataProvider.annotation.Return;

public class Arithmetic {

	@Info(name = "operator", desc = "返回四则运算结果")
	@Param(type = { "String" }, name = { "str" }, desc = { "四则运算表达式" })
	@Return(type = "String", desc = "四则运算公式")
	public static String operator(String str) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		InputStream in =Arithmetic.class.getResourceAsStream("/CalcEval.js");
	    
		try {
			Reader scriptReader = new InputStreamReader(in, "utf-8");
			engine.eval(scriptReader);
			Invocable invocable = (Invocable) engine;
			String  strOfObj =String.valueOf(invocable.invokeFunction("test", str));
			BigDecimal bd = new BigDecimal(strOfObj);
			return bd.toPlainString();
		} catch (ScriptException | NoSuchMethodException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	@Info(name = "md5Encode", desc = "返回md5加密结果")
	@Param(type = { "String" }, name = { "str" }, desc = { "加密字符串" })
	@Return(type = "String", desc = "返回md5加密结果")
	public static String md5Encode(String str) throws NoSuchAlgorithmException {
		if(null == str){
			return null;
		}
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		return new BigInteger(1, md.digest()).toString(16);
	}

	public static void main(String[] args) {
		try {
			System.out.println(md5Encode("it789123"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
