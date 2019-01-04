package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.RandomUtils;

public class MobileUtil {
	
	/**
	 * @api {函数} generateMobile() 生成手机号码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateMobile()}
	 */
	public static String generateMobile(){
		return RandomUtils.getInstance().generateMobilePhoneNumber();
	}

	/**
	 * @api {函数} getUnRegisterMobile_db() 未注册号码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterMobile_db()}
	 */
	public static void getUnRegisterMobile_db(){
	}

	public static String getUnRegisterMobile(String mySql_driver,String mySql_url,
											 String db_name,String db_password) throws SQLException, Exception{
		String mobile = RandomUtils.getInstance().generateMobilePhoneNumber();
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from qtpay.payuser WHERE userid='" + mobile + "'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		while (Integer.parseInt(count) > 0) {
			mobile = RandomUtils.getInstance().generateMobilePhoneNumber();
			sql = "select count(*) from qtpay.payuser WHERE userid='" + mobile + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return mobile;
	}

	/**
	 * @api {函数} getRegisterMobileRandom_db() 已注册号码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRegisterMobileRandom_db()}
	 */
	public static void getRegisterMobileRandom_db(String branchid){
	}

	public static String getRegisterMobileRandom(String mySql_driver, String mySql_url,
												 String db_name, String db_password, String branchid) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = 
//				"select t.userid FROM (SELECT userid FROM user_info WHERE userid is not null LIMIT 1000) t order by rand() LIMIT 1";
//		"select userid from (select t.userid FROM (SELECT userid FROM qtpay.payuser WHERE userid is not null and rownum < 1000) t " +
//        "order by dbms_random.value()) where rownum<2";
		
		"select userid from (" +
        "SELECT userid FROM qtpay.payuser WHERE branchid='" + branchid + "' and userid is not null and rownum < 1000 order by dbms_random.value())" +
        "where rownum<2";
		String mobile =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return mobile;
	}
}