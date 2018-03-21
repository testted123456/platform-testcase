package com.nonobank.testcase.utils.dll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DBUtils {

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
			dataSource.setMaxActive(25);//最大的连接数
			dataSource.setMaxIdle(5);//最大空闲数
			dataSource.setMinIdle(2);//最小空闲数
			dataSource.setValidationQuery("SELECT 1");
			dataSource.setTestWhileIdle(true);
			dataSource.setTimeBetweenEvictionRunsMillis(3600000);
			dataSource.setMinEvictableIdleTimeMillis(18000000);
			map.put(mySql_url + ":" + db_name, dataSource);
		}

		String name = db_name;
		Connection con = null;
		
		con = dataSource.getConnection();

		if (name.equals("nono")) {
			connection_nono_nono = con;
		} else if (name.equals("pay")) {
			connection_pay_nono = con;
		}
		return con;
	}

	public static void closeConnection(Connection con) throws SQLException {
		con.close();
	}

	/**
	 * 返回一个字段值
	 *
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Object getOneObject(Connection con, String sql) throws SQLException, Exception {
		Object[] objs = getOneLine(con, sql);

		if (objs.length == 0) {
			return null;
		}

		if (objs.length == 1) {
			return objs[0];
		} else {
			throw new Exception("获取的结果多余一个字段");
		}
	}

	/**
	 * 返回多个字段值
	 *
	 * @param con
	 * @param sql
	 * @return
	 */
	public static Object getMultObject(Connection con, String sql) throws SQLException{
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
			JSONArray jsonArray = (JSONArray) JSONArray.toJSON(objs);
			return jsonArray.toJSONString();
		}
	}

	// 插入一行sql
	public static Object[] insertOneObject(Connection con, String sql) throws SQLException{
		QueryRunner qr = new QueryRunner();
		Object[] objArr = null;
		
		try{
			objArr = qr.insert(con, sql, new ArrayHandler());
			return objArr;
		}catch(SQLException e){
			throw new SQLException(e.getMessage(), e.getCause());
		}finally {
			con.close();
		}
		
	}

	// 更新一行sql
	public static int updateOneObject(Connection con, String sql) throws SQLException{
		QueryRunner qr = new QueryRunner();
		int objArr = 0;
		try{
			objArr = qr.update(con, sql);
			return objArr;
		}catch(SQLException e){
			throw new SQLException(e.getMessage(), e.getCause());
		}finally {
			con.close();
		}

	}

	// 删除一行sql
	public static int deleteOneObject(Connection con, String sql) throws SQLException{
		QueryRunner qr = new QueryRunner();
		int objArr = 0;
		
		try{
			objArr = qr.update(con, sql);
			return objArr;
		}catch(SQLException e){
			throw new SQLException(e.getMessage(), e.getCause());
		}finally {
			con.close();
		}
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
		
		try{
			objArr = qr.query(con, sql, new ArrayHandler());
			return objArr;
		}catch(SQLException e){
			throw new SQLException(e.getMessage(), e.getCause());
		}finally {
			con.close();
		}
		
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
		return str.trim();
	}

	public String deleteValues(String mySql_driver, String mySql_url, String db_name, String db_password, String sql) throws SQLException{
		Connection con = getConnection(mySql_driver, mySql_url, db_name, db_password);
		int obj = deleteOneObject(con, sql);
		String str = Integer.toString(obj);
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
