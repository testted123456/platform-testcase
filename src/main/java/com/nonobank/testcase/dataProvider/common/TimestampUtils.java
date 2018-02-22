package com.nonobank.testcase.dataProvider.common;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.nonobank.testcase.dataProvider.annotation.Info;
import com.nonobank.testcase.dataProvider.annotation.Param;
import com.nonobank.testcase.dataProvider.annotation.Return;

public class TimestampUtils {
	
	@Info(name="getServerTime",desc="获取服务器时间")
	@Param(type={},name={},desc={})
	@Return(type="String",desc="获取服务器时间")
	public static String getServerTime() {
		Long times = System.currentTimeMillis();
		String timestamp = String.valueOf(times);
		return timestamp;
	}
	
	@Info(name="getServerTimeWithFormat",desc="指定时间格式，获取服务器时间")
	@Param(type={"String"},name={"timeFormat"},desc={"时间格式"})
	@Return(type="String",desc="指定时间格式，获取服务器时间")
	public static String getServerTimeWithFormat(String timeFormat)  {
		SimpleDateFormat dataFormat = new SimpleDateFormat(timeFormat);
		Date date = new Date();
		String dateString = dataFormat.format(date);
		return dateString;
	}
	
}
