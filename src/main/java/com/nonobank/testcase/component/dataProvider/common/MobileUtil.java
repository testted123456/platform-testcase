package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.MD5Util;
import com.nonobank.testcase.utils.dll.RandomUtils;

public class MobileUtil {

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
		String sql = "select count(*) from user_info WHERE mobile_num='" + mobile + "'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		while (Integer.parseInt(count) > 0) {
			mobile = RandomUtils.getInstance().generateMobilePhoneNumber();
			sql = "select count(*) from user_info WHERE mobile_num='" + mobile + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return mobile;
	}


	/**
	 * @api {函数} getUnRegisterAmericaMobile_db() 美国未注册号码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterAmericaMobile_db()}
	 */
	public static void getUnRegisterAmericaMobile_db(){
	}

	public static String getUnRegisterAmericaMobile(String mySql_driver,String mySql_url,
													String db_name,String db_password) throws SQLException, Exception{
		String mobile = RandomUtils.getInstance().generateAmericaMobile();
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE mobile_num='" + mobile + "'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		while (Integer.parseInt(count) > 0) {
			mobile = RandomUtils.getInstance().generateMobilePhoneNumber();
			sql = "select count(*) from user_info WHERE mobile_num='" + mobile + "'";
			count = String.valueOf(DBUtils.getOneObject(conn, sql));
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
	public static void getRegisterMobileRandom_db(){
	}

	public static String getRegisterMobileRandom(String mySql_driver,String mySql_url,
												 String db_name,String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		//String sql = "SELECT mobile_num FROM user_info WHERE is_card=0 AND id_num IS NULL AND STATUS=1 AND mobile_num is not null order by rand() LIMIT 1;";  //user_info表百万级，这句sql有性能问题
		String sql = "select t.mobile_num FROM (SELECT mobile_num FROM user_info WHERE is_card=1 AND STATUS=1 AND mobile_num is not null LIMIT 1000) t order by rand() LIMIT 1";
		String mobile =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return mobile;
	}

	/**
	 * @api {函数} getRealnameMobileRandom_db() 已实名号码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRealnameMobileRandom_db()}
	 */
	public static void getRealnameMobileRandom_db(){
	}

	public static String getRealnameMobileRandom(String mySql_driver,String mySql_url,
												 String db_name,String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "SELECT mobile_num FROM user_info WHERE is_card=1 AND STATUS=1 AND mobile_num is not null order by rand() LIMIT 1;";
		String mobile =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return mobile;
	}

	/**
	 * @api {函数} getBankcardMobile_db() 已绑卡号码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getBankcardMobile_db()}
	 */
	public static void getBankcardMobile_db(){
	}

	public static String getBankcardMobile(String mySql_driver,String mySql_url,
										   String db_name,String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select user_id from user_bankcard_info where bank_card_no is not null and is_deleted=0 limit 1";
		String user_id =  String.valueOf(DBUtils.getOneObject(conn, sql));
		Connection conn2 = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql2 = "select mobile_num from user_info where id='" + user_id + "'";
		String mobile =  String.valueOf(DBUtils.getOneObject(conn2, sql2));
		DBUtils.closeConnection(conn);
		return mobile;
	}

	/**
	 * @api {函数} getUserId_db("phoneNo") 按手机号获取UserId
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 手机号码
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUserId_db("180123123123")}
	 */
	public static void getUserId_db(String mobile){
	}

	public static String getUserId(String mySql_driver,String mySql_url,
								   String db_name,String db_password,String mobile) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select id from user_info where mobile_num='" + mobile + "'";
		String userId =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return userId;
	}

	/**
	 * @api {函数} getUserName_db("phoneNo") 按手机号获取UserName
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 测试参数
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUserName_db("180123123123")}
	 */
	public static void getUserName_db(String mobile){
	}

	public static String getUserName(String mySql_driver,String mySql_url,
									 String db_name,String db_password,String mobile) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select user_name from user_info where mobile_num='" + mobile + "'";
		String userName =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return userName;
	}

	/**
	 * @api {函数} getLoginPassword_db("mobile") 按手机号获取登陆密码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 手机号码
	 * @apiParam (入参) {String} requestXML 请求xml	
	 * @apiParam (入参) {String} id 用户id					
	 * @apiParamExample {String} Request-Example:
	 *    requestXML= 
	 *    	"<xml>
	 *    		<id>1234</id>
	 *    	<xml>"
	 * @apiSuccess (请求响应) {String} code 响应状态码
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     {
	 *       "code": "0000"
	 *     }
	 *     @apiSuccessExample {json} fail-Response:
	 *     HTTP/1.1 200 OK
	 *     {
	 *       "code": "0001"
	 *     }
	 */
	public static void getLoginPassword_db(String mobile){
	}

	public static String getLoginPassword(String mySql_driver,String mySql_url,
										  String db_name,String db_password,String mobile) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select password from user_info where mobile_num='" + mobile + "'";
		String loginPassword =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return loginPassword;
	}

	/**
	 * @api {函数} getPayPassword_db("mobile") 按手机号获取支付密码
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 手机号码
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getPayPassword_db("mobile")}
	 */
	public static void getPayPassword_db(){
	}

	public static String getPayPassword(String mySql_driver,String mySql_url,
										String db_name,String db_password,String mobile) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select pay_password from user_info where mobile_num='" + mobile + "'";
		String payPassword =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return payPassword;
	}

	/**
	 * @api {函数} getRealName_db("mobile") 按手机号获取真实姓名
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 手机号码
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRealName_db("mobile")}
	 */
	public static void getRealName_db(String mobile){
	}

	public static String getRealName(String mySql_driver,String mySql_url,
									 String db_name,String db_password,String mobile) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select real_name from user_info where mobile_num='" + mobile + "'";
		String realName =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return realName;
	}

	/**
	 * @api {函数} getIdCard_db("mobile") 按手机号获取身份证号
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 手机号码
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getIdCard_db("mobile")}
	 */
	public static void getIdCard_db(String mobile){
	}

	public static String getIdCard(String mySql_driver,String mySql_url,
								   String db_name,String db_password,String mobile) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select id_num from user_info where mobile_num='" + mobile + "'";
		String idCard =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return idCard;
	}

	/**
	 * @api {函数} md5MobileDate("mobile") MD5加密手机号码和当前日期
	 * @apiGroup MOBILE
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} mobile 手机号码
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${md5MobileDate("mobile")}
	 */
	public static String md5MobileDate(String mobile){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dt=new Date();
		String currentDate=format.format(dt);
//		String var =org.apache.commons.codec.digest.DigestUtils.md5Hex(mobile + currentDate);
		String var = MD5Util.toMD5(mobile + currentDate);
		return var;
	}
}