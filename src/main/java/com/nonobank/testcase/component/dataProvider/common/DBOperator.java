package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import com.nonobank.testcase.component.dataProvider.annotation.Info;
import com.nonobank.testcase.component.dataProvider.annotation.Param;
import com.nonobank.testcase.component.dataProvider.annotation.Return;
import com.nonobank.testcase.utils.dll.DBUtils;

public class DBOperator {
	
	@Info(name = "getOneField_db", desc = "getOneField_db(\"select * from user_info;\")")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql语句" })
	@Return(type = "String", desc = "查询数据库单个字段")
	public static void getOneField_db(String sql, String...args) {
	}

	public static String getOneField(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException, Exception{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = DBUtils.getOneObject(con, sql);
		return String.valueOf(obj);
	}

	@Info(name = "getOneField2", desc = "查询数据库一个字段")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "String", desc = "查询数据库一个字段")
	public static String getOneField2(String db_Config, String sql) throws SQLException, Exception{
		Connection con = DBUtils.createConByJson(db_Config);
		Object obj = DBUtils.getOneObject(con, sql);
		return String.valueOf(obj);
	}

	@Info(name = "getMultField_db", desc = "getMultField_db(\"select * from user_info;\")")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql语句" })
	@Return(type = "String", desc = "查询数据库多个字段")
	public static void getMultField_db(String sql) {
	}

	public static String getMultField(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = DBUtils.getMultObject(con, sql);
		return String.valueOf(obj);
	}

	@Info(name = "getMultField2", desc = "指定数据库信息，查询数据库多个字段")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "String", desc = "指定数据库信息，查询数据库多个字段")
	public static String getMultField2(String db_Config, String sql) throws SQLException{
		Connection con = DBUtils.createConByJson(db_Config);
		Object obj = DBUtils.getMultObject(con, sql);
		return String.valueOf(obj);
	}

	@Info(name = "insertValues_db", desc = "insertValues_db(\"insert into user_info values('1')\")")
	@Param(type = { "String" }, name = { "sql" }, desc = { "插入sql语句" })
	@Return(type = "String", desc = "返回插入的数据")
	public static void insertValues_db(String sql) {
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

	@Info(name = "insertValues2", desc = "插入表数据")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "String", desc = "返回插入的数据")
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

	@Info(name = "updateValues_db", desc = "updateValues_db(\"update user_info set user_name='user1' where id=1;\")")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql修改语句" })
	@Return(type = "String", desc = "返回被更新的行数")
	public static void updateValues_db(String sql) {
	}

	public static String updateValues(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException{
		Connection con = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		int obj = DBUtils.updateOneObject(con, sql);
		String str = Integer.toString(obj);
		return str;
	}

	@Info(name = "updateValues2", desc = "修改表数据")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "String", desc = "返回被更新的行数")
	public static String updateValues2(String db_Config, String sql) throws SQLException{
		Connection con = DBUtils.createConByJson(db_Config);
		int obj = DBUtils.updateOneObject(con, sql);
		String str = Integer.toString(obj);
		return str;
	}
}
