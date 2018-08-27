package com.nonobank.testcase.component.dataProvider.common;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Arithmetic {

	/**
	 * @api {函数} operator("str") 四则运算
	 * @apiGroup CAL
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} str 四则运算表达式
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${operator("1+2*5")}
	 */
	public static String operator(String str) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		InputStream in =Arithmetic.class.getResourceAsStream("/CalcEval.js");
		Reader scriptReader = new InputStreamReader(in, "utf-8");
		engine.eval(scriptReader);
		Invocable invocable = (Invocable) engine;
		String  strOfObj =String.valueOf(invocable.invokeFunction("test", str));
		BigDecimal bd = new BigDecimal(strOfObj);
		return bd.toPlainString();
	}

	/**
	 * @api {函数} md5Encode("str") md5加密
	 * @apiGroup CAL
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} str 待加密字符串
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${md5Encode("it789123")}
	 */
	public static String md5Encode(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		return new BigInteger(1, md.digest()).toString(16);
	}

}
