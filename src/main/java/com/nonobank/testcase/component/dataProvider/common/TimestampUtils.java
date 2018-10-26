package com.nonobank.testcase.component.dataProvider.common;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

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
	 * @api {函数} getServerTimeWithFormat("timeFormat") 按格式获取服务器时间,yyyyMMdd/HHmmss
	 * @apiGroup TIME
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} timeFormat 时间格式
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getServerTimeWithFormat("timeFormat")}
	 */
	public static String getServerTimeWithFormat(String timeFormat)  {
//		return LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(timeFormat));
		
		Date currentTime = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat(timeFormat); 
	    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	    String dateString = formatter.format(currentTime);
	    return dateString;
	}
	
	public static void main(String [] args){
		System.out.println(getServerTimeWithFormat("HHmmss"));
		System.out.println(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HHmmss")));
		
		Date currentTime = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");  
	    String dateString = formatter.format(currentTime);  
	    System.out.print(dateString);
		
	}

}
