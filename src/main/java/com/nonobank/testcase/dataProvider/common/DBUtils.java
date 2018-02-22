package com.nonobank.testcase.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.dataProvider.annotation.Info;
import com.nonobank.testcase.dataProvider.annotation.Param;
import com.nonobank.testcase.dataProvider.annotation.Return;

public class DBUtils {

	public static Logger logger = LoggerFactory.getLogger(DBUtils.class);
	
	public static Connection connection_nono_nono;
	public static Connection connection_pay_nono;
	public static Map<String, BasicDataSource> map = new HashMap<String, BasicDataSource>();

	/**
	 * @param
	 * @return
	 */
	public static Connection getConnection(String mySql_driver, String mySql_url, String db_name, String db_password)
			throws SQLException {
		
		BasicDataSource dataSource = map.get(mySql_url + ":" + db_name);
		
		if(null == dataSource){
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(mySql_driver);
			dataSource.setUrl(mySql_url);
			dataSource.setUsername(db_name);
			dataSource.setPassword(db_password);
			dataSource.setInitialSize(10);//初始化的连接数
			dataSource.setMaxActive(15);//最大的连接数
			dataSource.setMaxIdle(5);//最大空闲数
			dataSource.setMinIdle(2);//最小空闲数
			dataSource.setValidationQuery("SELECT 1");
			dataSource.setTestWhileIdle(true);
			dataSource.setTimeBetweenEvictionRunsMillis(3600000);
			dataSource.setMinEvictableIdleTimeMillis(18000000);
			map.put(mySql_url + ":" + db_name, dataSource);
		}

		String driver = mySql_driver;
		String url = mySql_url;
		String name = db_name;
		String password = db_password;

		Connection con = null;
		
		con = dataSource.getConnection();

		/**
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		con = DriverManager.getConnection(url, name, password);
		**/

		if (name.equals("nono")) {
			connection_nono_nono = con;
		} else if (name.equals("pay")) {
			connection_pay_nono = con;
		}
		return con;
	}

	public static void closeConnection(Connection con) {
		try {
			DbUtils.close(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		con = null;
	}

	/**
	 * 返回一个字段值
	 *
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Object getOneObject(Connection con, String sql) throws SQLException {
		Object[] objs = getOneLine(con, sql);

		if (objs.length == 0) {
			return null;
		}

		if (objs.length == 1) {
			return objs[0];
		} else {
			logger.info("get more than one objects.");
			return null;
		}
	}

	/**
	 * 返回多个字段值
	 *
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Object getMultObject(Connection con, String sql) throws SQLException {
		Object[] objs = getOneLine(con, sql);

		if (objs.length == 0) {
			return null;
		}

		for (int i = 0; i < objs.length; i++) {
			if(objs[i] instanceof Date){
				objs[i] = String.valueOf(objs[i]);
			}

		}

		if (objs.length == 1) {
			return objs[0];
		} else {
			// String restr = "";
			// for(int i=0;i<objs.length;i++){
			// restr = restr+String.valueOf(objs[i])+",";
			// }
			JSONArray jsonArray = (JSONArray) JSONArray.toJSON(objs);
			return jsonArray.toJSONString();
		}
	}

	// 插入一行sql
	public static Object[] insertOneObject(Connection con, String sql) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] objArr = null;
		objArr = qr.insert(con, sql, new ArrayHandler());
		return objArr;
	}

	// 更新一行sql
	public static int updateOneObject(Connection con, String sql) throws SQLException{
		QueryRunner qr = new QueryRunner();
		int objArr = 0;
		objArr = qr.update(con, sql);
		return objArr;

	}

	// 删除一行sql
	public static int deleteOneObject(Connection con, String sql) throws SQLException{
		QueryRunner qr = new QueryRunner();
		int objArr = 0;
		objArr = qr.update(con, sql);
		return objArr;
	}

	/**
	 * 返回一行记录
	 *
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Object[] getOneLine(Connection con, String sql) throws SQLException{

		QueryRunner qr = new QueryRunner();
		Object[] objArr = null;
		objArr = qr.query(con, sql, new ArrayHandler());
		return objArr;
	}

	@Info(name = "getOneField_db", desc = "查询数据库一个字段")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql语句" })
	@Return(type = "", desc = "")
	public static void getOneField_db(String sql) {
	}

	public static String getOneField(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = getOneObject(con, sql);
		DbUtils.close(con);
		return String.valueOf(obj);
	}

	@Info(name = "getOneField2", desc = "查询数据库一个字段")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "", desc = "")
	public static String getOneField2(String db_Config, String sql) throws SQLException{
		Connection con = createConByJson(db_Config);
		Object obj = getOneObject(con, sql);
		DbUtils.close(con);
		return String.valueOf(obj);
	}

	@Info(name = "getMultField_db", desc = "查询数据库多个字段")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql语句" })
	@Return(type = "", desc = "")
	public static void getMultField_db(String sql) {
	}

	public static String getMultField(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = getMultObject(con, sql);
		DbUtils.close(con);
		return String.valueOf(obj);
	}

	@Info(name = "getMultField2", desc = "指定数据库信息，查询数据库多个字段")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "", desc = "")
	public static String getMultField2(String db_Config, String sql) throws SQLException{
		Connection con = createConByJson(db_Config);
		Object obj = getMultObject(con, sql);
		DbUtils.close(con);
		return String.valueOf(obj);
	}

	public String getValues(String mySql_driver, String mySql_url, String db_name, String db_password, String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = getOneLine(con, sql);
		String str = null;
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
		DbUtils.close(con);
		return str.trim();
	}

	@Info(name = "insertValues_db", desc = "插入表数据")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql插入方法" })
	@Return(type = "", desc = "")
	public static void insertValues_db(String sql) {
	}

	public static String insertValues(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		Object obj = insertOneObject(con, sql);
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
		DbUtils.close(con);
		return str.trim();
	}

	@Info(name = "insertValues2", desc = "插入表数据")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "", desc = "")
	public static String insertValues2(String db_Config, String sql) throws SQLException{
		Connection con = createConByJson(db_Config);
		Object obj = insertOneObject(con, sql);
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
		DbUtils.close(con);
		return str.trim();
	}

	@Info(name = "updateValues_db", desc = "修改表数据")
	@Param(type = { "String" }, name = { "sql" }, desc = { "sql修改语句" })
	@Return(type = "", desc = "")
	public static void updateValues_db(String sql) {
	}

	public static String updateValues(String mySql_driver, String mySql_url, String db_name, String db_password,
			String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		int obj = updateOneObject(con, sql);
		String str = Integer.toString(obj);
		DbUtils.close(con);
		return str;
	}

	@Info(name = "updateValues2", desc = "修改表数据")
	@Param(type = { "String", "String" }, name = { "db_Config", "sql" }, desc = { "数据源配置", "sql语句" })
	@Return(type = "", desc = "")
	public static String updateValues2(String db_Config, String sql) throws SQLException{
		Connection con = createConByJson(db_Config);
		int obj = updateOneObject(con, sql);
		String str = Integer.toString(obj);
		DbUtils.close(con);
		return str;
	}

	public String deleteValues(String mySql_driver, String mySql_url, String db_name, String db_password, String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		int obj = deleteOneObject(con, sql);
		String str = Integer.toString(obj);
		DbUtils.close(con);
		return str;
	}

	public static List<String> getMoreLine(Connection con, String sql) throws SQLException{

		QueryRunner qr = new QueryRunner();

			List rs = qr.query(con, sql, new ArrayListHandler());
			for (Object user : rs) {
				System.out.println(Arrays.toString((Object[]) user));
			}
			return rs;

	}

	public static Connection createConByJson(String db_Config) throws SQLException{
		JSONObject json = (JSONObject) JSONObject.parse(db_Config);
		String ip = json.getString("ip");
		String port = json.getString("port");
		String dataBaseName = json.getString("dataBaseName");
		String user_name = json.getString("user_name");
		String db_password = json.getString("db_password");
		Connection con = getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://" + ip + ":" + port + "/" + dataBaseName,
				user_name, db_password);
		return con;
	}

}
