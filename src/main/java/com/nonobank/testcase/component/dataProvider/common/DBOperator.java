package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import com.nonobank.testcase.utils.dll.DBUtils;

public class DBOperator {

	/**
	 * @api {函数} getOneField_db("sql") 单字段查询
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} sql sql语句
	 * @apiParam (入参) {String} args 可选参数，数据库配置名称，默认default
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getOneField_db("select name from user_info", "default")}
	 */
	public static void getOneField_db(String sql, String...args) {
	}

	public static String getOneField(String mySql_driver, String mySql_url, String db_name, String db_password,
									 String sql) throws SQLException, Exception{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = DBUtils.getOneObject(con, sql);
		return String.valueOf(obj);
	}

	/**
	 * @api {函数} getOneField2("db_Config","sql") 按数据源查询单字段
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} db_Config 数据源别名，在平台中已配好的数据源
	 * @apiParam (入参) {String} sql sql语句
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getOneField2("STBDS", "select name from user_info;")}
	 */
	public static String getOneField2(String db_Config, String sql) throws SQLException, Exception{
		Connection con = DBUtils.createConByJson(db_Config);
		Object obj = DBUtils.getOneObject(con, sql);
		return String.valueOf(obj);
	}

	/**
	 * @api {函数} getMultField_db("sql") 多字段查询
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} sql sql语句
	 * @apiParam (入参) {String} args 可选参数，数据库配置名称，默认default
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getMultField_db("select id,name from user_info;", "default")}
	 */
	public static void getMultField_db(String sql, String...args) {
	}

	public static String getMultField(String mySql_driver, String mySql_url, String db_name, String db_password,
									  String sql) throws SQLException{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = DBUtils.getMultObject(con, sql);
		return String.valueOf(obj);
	}

	/**
	 * @api {函数} getMultField2("db_Config","sql") 按数据源查询多字段
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} db_Config 数据源别名，在平台中已配好的数据源
	 * @apiParam (入参) {String} sql sql语句
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getMultField2("STBDS", "select id,name from user_info;")}
	 */
	public static String getMultField2(String db_Config, String sql) throws SQLException{
		Connection con = DBUtils.createConByJson(db_Config);
		Object obj = DBUtils.getMultObject(con, sql);
		return String.valueOf(obj);
	}

	/**
	 * @api {函数} insertValues_db("sql") 插数据
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} sql 插入sql语句
	 * @apiParam (入参) {String} args 可选参数，数据库配置名称，默认default
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${insertValues_db("insert into user_info values('1')", "default")}
	 */
	public static void insertValues_db(String sql, String...args) {
	}

	public static String insertValues(String mySql_driver, String mySql_url, String db_name, String db_password,
									  String sql) throws SQLException{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = DBUtils.insertOneObject(con, sql);
		String str = "";
		if (obj instanceof Object[]) {
			Object[] objs = (Object[]) obj;

			for (int i = 0; i < objs.length; i++) {
				if (i == 0) {
					str = objs[i].toString();
				} else {
					str = str + "," + objs[i];
				}
			}
		}
		return str.trim();
	}

	/**
	 * @api {函数} insertValues2("db_Config","sql") 按数据源插入数据
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} db_Config 数据源别名，在平台中已配好的数据源
	 * @apiParam (入参) {String} sql 插入语句
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${insertValues2("STBDS", "insert into user_info values('1')")}
	 */
	public static String insertValues2(String db_Config, String sql) throws SQLException{
		Connection con = DBUtils.createConByJson(db_Config);
		Object obj = DBUtils.insertOneObject(con, sql);
		String str = "";
		if (obj instanceof Object[]) {
			Object[] objs = (Object[]) obj;

			for (int i = 0; i < objs.length; i++) {
				if (i == 0) {
					str = objs[i].toString();
				} else {
					str = str + "," + objs[i];
				}
			}
		}
		return str.trim();
	}

	/**
	 * @api {函数} updateValues_db("sql") 修改数据
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} sql sql修改语句
	 * @apiParam (入参) {String} args 可选参数，数据库配置名称，默认default
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${updateValues_db("update user_info set user_name='user1' where id=1;")}
	 */
	public static void updateValues_db(String sql, String...args) {
	}

	public static String updateValues(String mySql_driver, String mySql_url, String db_name, String db_password,
									  String sql) throws SQLException{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		int obj = DBUtils.updateOneObject(con, sql);
		String str = Integer.toString(obj);
		return str;
	}

	/**
	 * @api {函数} updateValues2("db_Config","sql") 按数据源修改数据
	 * @apiGroup DB_OPER
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} db_Config 数据源别名，在平台中已配好的数据源
	 * @apiParam (入参) {String} sql 修改语句
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${updateValues2("STBDS", "update user_info set user_name='user1' where id=1;")}
	 */
	public static String updateValues2(String db_Config, String sql) throws SQLException{
		Connection con = DBUtils.createConByJson(db_Config);
		int obj = DBUtils.updateOneObject(con, sql);
		String str = Integer.toString(obj);
		return str;
	}
}