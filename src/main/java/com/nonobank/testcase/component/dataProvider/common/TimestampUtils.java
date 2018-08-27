package com.nonobank.testcase.component.dataProvider.common;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtils {

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

	/**
	 * @api {函数} getServerTimeWithFormat("timeFormat") 按格式获取服务器时间,YYMMDD/hhmmss
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
