package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import com.nonobank.testcase.utils.dll.DBUtils;

public class RateCutCodeUtils {

	public static String cutCode(){
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<11;i++){
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * @api {函数} getRateCutCode_db() 获得未使用的减息券
	 * @apiGroup OTHER
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRateCutCode_db()}
	 */
	public static void getRateCutCode_db(){
	}

	public static String getRateCutCode(String mySql_driver,String mySql_url,
										String db_name,String db_password) throws SQLException{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String code = RateCutCodeUtils.cutCode();
		String sql = "INSERT INTO user_InterestrateCut(code,cutValue,overdue_time,suit_products,money_limit,expect_limit,education,consultFee,remark) VALUE('"+code+"','10','2016-09-05 23:59:59','名校贷,名校贷应急包,白领包,专科包','100-50000','1,2,3,12,24,36','专科,本科,硕士研究生,博士研究生','2','测试专用')";
		DBUtils.insertOneObject(conn, sql);
		DBUtils.closeConnection(conn);
		return code;
	}
}