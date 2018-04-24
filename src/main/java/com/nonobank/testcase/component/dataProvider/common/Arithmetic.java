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
import com.nonobank.testcase.component.dataProvider.annotation.Info;
import com.nonobank.testcase.component.dataProvider.annotation.Param;
import com.nonobank.testcase.component.dataProvider.annotation.Return;

public class Arithmetic {

	@Info(name = "operator", desc = "operator(\"1+2*5\")")
	@Param(type = { "String" }, name = { "str" }, desc = { "四则运算表达式" })
	@Return(type = "String", desc = "返回四则运算结果")

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

	@Info(name = "md5Encode", desc = "md5Encode(\"it789123\")")
	@Param(type = { "String" }, name = { "str" }, desc = { "待加密字符串" })
	@Return(type = "String", desc = "返回md5加密结果")

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

	@Info(name = "get36Hex1", desc = "get36Hex1(\"123\") ")
	@Param(type = { "String" }, name = { "bId" }, desc = { "bpId" })
	@Return(type = "String", desc = "根据bpId生成bo_identity")

	/**
	 * @api {函数} get36Hex1("bId") 生成bo_identity
	 * @apiGroup CAL
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} bId bpId
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${get36Hex1("123")}
	 */
	public static String get36Hex1(String bId) {
		Long bpId = Long.valueOf(bId);
		if (bpId > Integer.MAX_VALUE) {
			// 测试环境出现了bpId超过Integer.MAX_VALUE的情况,所以写了这个判断,测试专用
			String bpIdStr = String.valueOf(bpId);
			System.out.println(bpIdStr.substring(bpIdStr.length() - 6));
			return bpIdStr.substring(bpIdStr.length() - 6);
		}

		// 目前的业务量不会超过Integer.MAX_VALUE,所以可以这么写
		int bpIdInt = bpId.intValue();

		String hex36 = "0123456789abcdefghijklmnopqrstuvwxyz";

		// 10进制转换成36进制
		String result = "";
		while (bpIdInt >= 36) {
			result = hex36.charAt((bpIdInt % 36)) + result;
			bpIdInt /= 36;
		}
		if (bpIdInt >= 0)
			result = hex36.charAt(bpIdInt) + result;
		// 当36进制的数字<6位时,补0
		int length = 6 - result.length();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				result = "0" + result;
			}
		}
		return result;
	}


}
