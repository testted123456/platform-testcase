package com.nonobank.testcase.component.dataProvider.common;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.nonobank.testcase.component.dataProvider.annotation.Info;
import com.nonobank.testcase.component.dataProvider.annotation.Param;
import com.nonobank.testcase.component.dataProvider.annotation.Return;

public class TimestampUtils {

	@Info(name="getServerTime",desc="获取服务器时间")
	@Param(type={},name={},desc={})
	@Return(type="String",desc="获取服务器时间")

	/**
	 * @api {函数} getServerTime() 获取服务器时间
	 * @apiGroup TIME
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getServerTime()}
	 */
	public static String getServerTime() {
		Long times = System.currentTimeMillis();
		String timestamp = String.valueOf(times);
		return timestamp;
	}

	@Info(name="getServerTimeWithFormat",desc="指定时间格式，获取服务器时间")
	@Param(type={"String"},name={"timeFormat"},desc={"时间格式"})
	@Return(type="String",desc="指定时间格式，获取服务器时间")

	/**
	 * @api {函数} getServerTimeWithFormat("timeFormat") 按格式获取服务器时间
	 * @apiGroup TIME
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} timeFormat 时间格式
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getServerTimeWithFormat("timeFormat")}
	 */
	public static String getServerTimeWithFormat(String timeFormat)  {
		SimpleDateFormat dataFormat = new SimpleDateFormat(timeFormat);
		Date date = new Date();
		String dateString = dataFormat.format(date);
		return dateString;
	}

}
