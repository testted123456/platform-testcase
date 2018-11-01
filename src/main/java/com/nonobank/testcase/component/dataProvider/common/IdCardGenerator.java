package com.nonobank.testcase.component.dataProvider.common;

import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.IdCardGeneratorUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
		String sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
		String count = String.valueOf(DBUtils.getOneObject(conn, sql));

		while (Integer.parseInt(count) > 0) {
			generater = new StringBuilder();
			generater.append(code);
			generater.append(birthDay);
			generater.append(IdCardGeneratorUtil.randomCode());
			generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
			idCard = generater.toString();
			sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
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
		String sql = "select customerpid from qtpay.paycustomer WHERE customerpid like '" + prefixOfArea + prefixOfBirthDay + "%'";
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
		String sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();
		while (Integer.parseInt(count) > 0) {
			idCard = IdCardGenerator.generate();
			sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
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
		String sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();
		while (Integer.parseInt(count) > 0) {
			idCard = IdCardGenerator.generate();
			sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
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
		String sql = "SELECT customerpid FROM qtpay.paycustomer where customerpid is not null order by dbms_random.value()";
		sql = "SELECT customerpid FROM (" + sql + ") where rownum<2";
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
		String sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();
		while (Integer.parseInt(count) > 0) {
			idCard = IdCardGenerator.generate();
			sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
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

	public static String generateByProvinceCityDistrict(String province, String city, String district) {
		StringBuilder generater = new StringBuilder();
		int areaCode = IdCardGeneratorUtil.getAreaCode(province,city, district);
		if (areaCode < 0){
			return null;
		}else {
			generater.append(IdCardGeneratorUtil.getAreaCode(province, city, district));
		}
		generater.append(IdCardGeneratorUtil.randomBirthday());
		generater.append(IdCardGeneratorUtil.randomCode());
		generater.append(IdCardGeneratorUtil.calcTrailingNumber(generater.toString().toCharArray()));
		return generater.toString();

	}

	/**
	 *
	 * @param mySql_driver e.g.com.mysql.jdbc.Driver
	 * @param mySql_url 测试环境MySQL数据库连接串
	 * @param db_name 数据库用户名
	 * @param db_password 数据库密码
	 * @param province 省份(直辖市)
	 * @param city  城市
	 * @param district  区县
	 * @return  未注册身份证号
	 * @throws SQLException
	 * @throws Exception
	 */
	public static String getUnRegisterIDCardByProvinceCityDistrict(String mySql_driver,String mySql_url,
													 String db_name,
													 String db_password,
													 String province,
													 String city,
													 String district) throws SQLException, Exception{
		String idCard = IdCardGenerator.generateByProvinceCityDistrict(province,city, district);
		if (idCard == null){
			return null;
		}
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		String sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
		String count = DBUtils.getOneObject(conn, sql).toString();

		int loop = 0;
		while (Integer.parseInt(count) > 0 && loop < 100) {
			idCard = IdCardGenerator.generateByProvinceCityDistrict(province,city, district);
			if (idCard == null){
				return null;
			}
			sql = "select count(*) from qtpay.paycustomer WHERE customerpid='" + idCard + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
			loop++;
		}
		DBUtils.closeConnection(conn);
		return idCard;
	}

	/**
	 *
	 * @param mySql_driver  e.g.com.mysql.jdbc.Driver
	 * @param mySql_url  测试环境MySQL数据库连接串
	 * @param db_name  数据库用户名
	 * @param db_password  数据库密码
	 * @param province  省份(直辖市)
	 * @param city  城市
	 * @param district  区县
	 * @return 已注册身份证号
	 */
	public static String getRegisterIDCardByProvinceCityDistrict(String mySql_driver,
																 String mySql_url,
																 String db_name,
																 String db_password,
																 String province,
																 String city,
																 String district
																 )throws Exception{

		/*if (province == null || province.isEmpty()){
			return getRegisterIDCardRandom(mySql_driver, mySql_url,db_name,db_password);
		}

		String pattern = null;
		String provinceCode = IdCardGeneratorUtil.getProvinceCode(province);
		if (provinceCode == null){
			//省份(直辖市)不匹配
			return null;
		}

		if (city == null || city.isEmpty()){
			pattern = "^" + provinceCode.substring(0, 2);
		}else{
			String cityCode = IdCardGeneratorUtil.getCityCode(province, city);
			if (cityCode == null){
				//城市不匹配
				return null;
			}

			if (district == null || district.isEmpty()){
				pattern = "^" + cityCode.substring(0, 4);
			}else {
				Map.Entry<Integer, String> entryDistrict = IdCardGeneratorUtil.areaCode.entrySet().stream().filter(e -> {
					String key = String.valueOf(e.getKey());
					return e.getValue().equals(district) && Pattern.compile("^" + cityCode.substring(0, 4) + "\\d+$").matcher(key).find();
				}).findFirst().orElse(null);

				String districtCode = entryDistrict == null ? null : String.valueOf(entryDistrict.getKey());
				if (districtCode == null) {
					//区县不匹配
					return null;
				} else {
					pattern = "^" + districtCode;
				}
			}
		}*/

		String sql = "SELECT customerpid FROM (SELECT customerpid FROM qtpay.paycustomer order by dbms_random.value()) where rownum<2";
		Connection conn = DBUtils.getConnection(mySql_driver,mySql_url,db_name,db_password);
		Object obj = DBUtils.getOneObject(conn, sql);
		String id = String.valueOf(obj);
		
		for(int i=0;i<100;i++){
			if(null != id && !id.equals("null")){
				break;
			}else{
				obj = DBUtils.getOneObject(conn, sql);
				id = String.valueOf(obj);
			}
		}

		return id;
	}
	
	public static void main(String [] args){
		System.out.println(generateByYearEndWithX("1990"));
	}

}