package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.IdCardGeneratorUtil;

public class IdCardGenerator {
	public static final Map<String, Integer> areaCode = new HashMap<String, Integer>();

	/**
	 * @api {函数} generate() 随机生成身份证号
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generate()}
	 */
	public static String generate() {
		StringBuilder generater = new StringBuilder();
		generater.append(IdCardGeneratorUtil.randomAreaCode());
		generater.append(IdCardGeneratorUtil.randomBirthday());
		generater.append(IdCardGeneratorUtil.randomCode());
		generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
		return generater.toString();
	}

	/**
	 * @api {函数} generateByYear(year) 按年生成身份证号
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {int} year 出生年份
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateByYear("1982")}
	 */
	public static String generateByYear(String year) {
		int int_year=Integer.parseInt(year);
		StringBuilder generater = new StringBuilder();
		generater.append(IdCardGeneratorUtil.randomAreaCode());
		generater.append(IdCardGeneratorUtil.generateBirthdayByYear(int_year));
		generater.append(IdCardGeneratorUtil.randomCode());
		generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
		return generater.toString();
	}

	/**
	 * @api {函数} generateByYear("area") 按地区生成身份证号
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} area 地区
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateByYear("xxxx")}
	 */
	public static String generateByArea(String area){
		Integer code = areaCode.get(area);

		if(null == code){
			return null;
		}

		StringBuilder generater = new StringBuilder();
		generater.append(code);
		generater.append(IdCardGeneratorUtil.randomBirthday());
		generater.append(IdCardGeneratorUtil.randomCode());
		generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
		return generater.toString();
	}

	/**
	 * 根据地区、出生年月生成未使用的身份证
	 * @param mySql_driver
	 * @param mySql_url
	 * @param db_name
	 * @param db_password
	 * @param area
	 * @param birthDay
	 * @return
	 */
	public static String generateUnUsedByAll(String mySql_driver,String mySql_url,
											 String db_name,String db_password, String area, String birthDay) throws SQLException, Exception{
		Integer code = null;

		if(null == area){
			code = IdCardGeneratorUtil.randomAreaCode();
		}else{
			code = areaCode.get(area);
		}

		if(null == birthDay){
			birthDay = IdCardGeneratorUtil.randomBirthday();
		}

		if(null == code){
			return null;
		}

		StringBuilder generater = new StringBuilder();
		generater.append(code);
		generater.append(birthDay);
		generater.append(IdCardGeneratorUtil.randomCode());
		generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
		String idCard = generater.toString();

		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
		String count = String.valueOf(DBUtils.getOneObject(conn, sql));

		while (Integer.parseInt(count) > 0) {
			generater = new StringBuilder();
			generater.append(code);
			generater.append(birthDay);
			generater.append(IdCardGeneratorUtil.randomCode());
			generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
			idCard = generater.toString();
			sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return generater.toString();
	}

	/**
	 * 根据地区、出生年月生成已使用的身份证
	 * @param mySql_driver
	 * @param mySql_url
	 * @param db_name
	 * @param db_password
	 * @param area
	 * @param birthDay
	 * @return
	 */
	public static String generateUsedByAll(String mySql_driver,String mySql_url,
										   String db_name,String db_password, String area, String birthDay) throws SQLException, Exception{
		String prefixOfArea = null;
		String prefixOfBirthDay = null;

		if(null == area){
			prefixOfArea = "______";
		}else{
			Integer code = areaCode.get(area);

			if(null == code){
				return null;
			}else{
				prefixOfArea = code.toString();
			}
		}

		if(null == birthDay){
			prefixOfBirthDay = "________";
		}else{
			prefixOfBirthDay = birthDay;
		}

		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select id_num from user_info WHERE id_num like '" + prefixOfArea + prefixOfBirthDay + "%'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return count;
	}

	/**
	 * @api {函数} getUnRegisterIDCard_db() 未使用的身份证号
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterIDCard_db()}
	 */
	public static void getUnRegisterIDCard_db(){
	}

	public static String getUnRegisterIDCard(String mySql_driver,String mySql_url,
											 String db_name,String db_password) throws SQLException, Exception{
		String idCard = IdCardGenerator.generate();
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();
		while (Integer.parseInt(count) > 0) {
			idCard = IdCardGenerator.generate();
			sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return idCard;
	}

	/**
	 * @api {函数} getUnRegisterByAdultIDCard_db() 未使用的身份证号码(16-40岁)
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterByAdultIDCard_db()}
	 */
	public static void getUnRegisterByAdultIDCard_db(){
	}

	public static String getUnRegisterByAdultIDCard(String mySql_driver,String mySql_url,
													String db_name,String db_password) throws SQLException, Exception{
		Random rand = new Random();
		int year = rand.nextInt(23)+1977;
		String idCard = IdCardGenerator.generateByYear(String.valueOf(year));
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();
		while (Integer.parseInt(count) > 0) {
			idCard = IdCardGenerator.generate();
			sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return idCard;
	}

	/**
	 * @api {函数} getRegisterIDCardRandom_db() 已注册的身份证号
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0

	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getRegisterIDCardRandom_db()}
	 */
	public static void getRegisterIDCardRandom_db(){
	}

	public static String getRegisterIDCardRandom(String mySql_driver,String mySql_url,
												 String db_name,String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "SELECT id_num FROM user_info order by rand() LIMIT 1;";
		String idCard =  String.valueOf(DBUtils.getOneObject(conn, sql));
		return idCard;
	}

	/**
	 * @api {函数} getUnRegisterIDCardByYear_db("year") 按年生成未注册的身份证号
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} year 出生年份
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${getUnRegisterIDCardByYear_db("1990")}
	 */
	public static void getUnRegisterIDCardByYear_db(String year){
	}

	public static String getUnRegisterIDCardByYear(String mySql_driver,String mySql_url,
												   String db_name,String db_password,String year) throws SQLException, Exception{
		String idCard = IdCardGenerator.generateByYear(year);
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();
		while (Integer.parseInt(count) > 0) {
			idCard = IdCardGenerator.generate();
			sql = "select count(*) from user_info WHERE id_num='" + idCard + "'";
			count = String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return idCard;
	}
	
	/**
	 * @api {函数} generateEndWithX("year") 生成X结尾的身份证
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateEndWithX()}
	 */
	public static String generateEndWithX(){
		String id = generate();
		
		while(!id.endsWith("X")){
			id = generate();
		}
		
		return id;
	}
	
	
	/**
	 * @api {函数} generateByYearEndWithX("year") 根据年份生成X结尾的身份证
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} year 出生年份
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateByYearEndWithX("1990")}
	 */
	public static String generateByYearEndWithX(String year){
		String id = generateByYear(year);
		
		while(!id.endsWith("X")){
			id = generateByYear(year);
		}
		
		return id;
	}
	
	/**
	 * @api {函数} generateByYearEndWithLowerCase("year") 根据年份生成x结尾的身份证
	 * @apiGroup IDCARD
	 * @apiVersion 0.1.0
	 * @apiParam (入参) {String} year 出生年份
	 * @apiSuccessExample {invoke} 调用说明:
	 * ${generateByYearEndWithLowerCase("1990")}
	 */
	public static String generateByYearEndWithLowerCase(String year){
		String id = generateByYearEndWithX(year);
		return id.toLowerCase();
	}

}