package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.RandomUtils;

public class UserUtils {

	/**
	 * @api {函数} getUnRegisterUserName_db() 获取未注册的用户名
	 * @apiGroup USER
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterUserName_db()}
	 */
	public static void getUnRegisterUserName_db(){
	}

	public static String getUnRegisterUserName(String mySql_driver,String mySql_url,
											   String db_name,String db_password) throws SQLException, Exception{
		String username = RandomUtils.getInstance().generateUserName();
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE user_name='" + username + "'";
		String count = String.valueOf(DBUtils.getOneObject(conn, sql));
		while (Integer.parseInt(count) > 0) {
			username = RandomUtils.getInstance().generateMobilePhoneNumber();
			sql = "select count(*) from user_info WHERE user_name='" + username + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return username;
	}

	/**
	 * @api {函数} getRegisterUserNameByRandom_db() 获取已经存在的用户名
	 * @apiGroup USER
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRegisterUserNameByRandom_db()}
	 */
	public static void getRegisterUserNameByRandom_db(){
	}

	public static String getRegisterUserNameByRandom(String mySql_driver,String mySql_url,
													 String db_name,String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "SELECT user_name FROM user_info WHERE user_name is not null order by rand() LIMIT 1;";
		String mobile =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return mobile;
	}

	/**
	 * @api {函数} getUnRegisterEmail_db() 获取未使用的email
	 * @apiGroup USER
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterEmail_db()}
	 */
	public static void getUnRegisterEmail_db(){
	}

	public static String getUnRegisterEmail(String mySql_driver,String mySql_url,
											String db_name,String db_password) throws SQLException, Exception{
		String email = RandomUtils.getInstance().generateRandomEmail();
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE email='" + email + "'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		while (Integer.parseInt(count) > 0) {
			email = RandomUtils.getInstance().generateMobilePhoneNumber();
			sql = "select count(*) from user_info WHERE email='" + email + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return email;
	}

	/**
	 * @api {函数} getRegisterEmailByRandom_db() 获取已经存在的email
	 * @apiGroup USER
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRegisterEmailByRandom_db()}
	 */
	public static void getRegisterEmailByRandom_db(){
	}

	public static String getRegisterEmailByRandom(String mySql_driver,String mySql_url,
												  String db_name,String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "SELECT email FROM user_info WHERE email is not null order by rand() LIMIT 1;";
		String email =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return email;
	}
}
