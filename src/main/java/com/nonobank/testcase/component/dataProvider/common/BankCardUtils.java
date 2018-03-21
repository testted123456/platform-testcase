package com.nonobank.testcase.component.dataProvider.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.nonobank.testcase.component.dataProvider.annotation.Info;
import com.nonobank.testcase.component.dataProvider.annotation.Param;
import com.nonobank.testcase.component.dataProvider.annotation.Return;
import com.nonobank.testcase.utils.dll.DBUtils;

public class BankCardUtils {
	// 436742121737-建设银行---------------CCB
	// 622696779515-中信银行---------------CITIC
	// 622632349194-华夏银行---------------HXB
	// 622568683546-广发银行 --------------GDB
	// 622298316429-平安银行---------------PAB

	// 622827469345-农业银行---------------ABC
	// 620061511669-中国银行---------------BOC
	// 621485021040-招商银行---------------CMB
	// 622660633366-光大银行---------------CEB
	// 622262406933-交通银行---------------BCOM

//	 621691506392-民生银行---------------CMBC
	
	// 622521071652-浦发银行---------------SPDB
	// 622202100112-工商银行---------------ICBC
	// 622908192090-兴业银行---------------CIB
	// 621799417365-邮政储蓄---------------PSBC
	
	static Map<String, String> bankMap = new HashMap<String, String>();

	static {
		bankMap.put("CCB", "436742121737");
		bankMap.put("CITIC", "622696779515");
		bankMap.put("HXB", "622632349194");
		bankMap.put("GDB", "622568683546");
		bankMap.put("PAB", "622298316429");
		bankMap.put("ABC", "622827469345");
		bankMap.put("BOC", "620061511669");
		bankMap.put("CMB", "621485021040");
		bankMap.put("CEB", "622660633366");
		bankMap.put("BCOM", "621691506392");
		bankMap.put("SPDB", "622521071652");
		bankMap.put("ICBC", "622202100112");
		bankMap.put("CIB", "622908192090");
		bankMap.put("PSBC", "621799417365");
	}

//	static {
//		bankMap.put("建设银行", "436742121737");
//		bankMap.put("中信银行", "622696779515");
//		bankMap.put("华夏银行", "622632349194");
//		bankMap.put("广发银行", "622568683546");
//		bankMap.put("平安银行", "622298316429");
//		bankMap.put("农业银行", "622827469345");
//		bankMap.put("中国银行", "620061511669");
//		bankMap.put("招商银行", "621485021040");
//		bankMap.put("光大银行", "622660633366");
//		bankMap.put("民生银行", "621691506392");
//		bankMap.put("浦发银行", "622521071652");
//		bankMap.put("工商银行", "622202100112");
//		bankMap.put("兴业银行", "622908192090");
//		bankMap.put("邮政银行", "621799417365");
//	}
	
	
	private static String[] prefix = { "436742121737", "622696779515", "622632349194", "622568683546", "622298316429",
			"622827469345", "620061511669", "621485021040", "622660633366", "622660633366", "622262406933",
			"621691506392", "622521071652", "622202100112", "622908192090", "621799417365" };
	
	//返回支持的银行名称
	public static List<String> getBankNames(){
		Set<String> bankNames = bankMap.keySet();
		List<String> list = new LinkedList<>();
		list.addAll(bankNames);
		return list;
	}

	public static int getOSum(int oSum) {
		oSum = oSum * 2;
		if (oSum < 10) {
			return oSum;
		} else {
			int m = oSum % 10;
			return (oSum - m) / 10 + m;
		}
	}

	public static String getBankCard(String prefix) {
		Random random = new Random();
		String str = prefix;
		for (int i = 0; i < 6; i++) {
			str += String.valueOf(random.nextInt(10));
		}
		int sum = 0;
		for (int i = 1; i < 19; i++) {
			int v = Character.getNumericValue(str.charAt(i - 1));
			if (i % 2 == 0) {
				sum += getOSum(v);
			} else {
				sum += v;
			}
		}
		str += String.valueOf(10 - sum % 10);
		return str;
	}

	public static String getBankCard() {
		Random random = new Random();
		String str = prefix[random.nextInt(2)];
		for (int i = 0; i < 6; i++) {
			str += String.valueOf(random.nextInt(10));
		}
		int sum = 0;
		for (int i = 1; i < 19; i++) {
			int v = Character.getNumericValue(str.charAt(i - 1));
			if (i % 2 == 0) {
				sum += getOSum(v);
			} else {
				sum += v;
			}
		}
		str += String.valueOf(10 - sum % 10);
		return str;
	}

	@Info(name = "getUnUseBankcard_db", desc = "getUnUseBankcard_db()")
	@Param(type = { }, name = { }, desc = {  })
	@Return(type = "String", desc = "获取未使用的银行卡号码")
	public static void getUnUseBankcard_db(){
	}
	
	public static String getUnUseBankcard(String mySql_driver, String mySql_url, String db_name, String db_password) throws SQLException, Exception{
		String bankCard = BankCardUtils.getBankCard("621226100102");
		Connection conn = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		String sql = "select count(*) from user_bankcard_info WHERE bank_card_no='" + bankCard + "'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		while (Integer.parseInt(count) > 0) {
			bankCard = BankCardUtils.getBankCard();
			sql = "select count(*) from user_bankcard_info WHERE bank_card_no='" + bankCard + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return bankCard;
	}
	
	@Info(name = "getBankcardByBankName", desc = "getBankcardByBankName(\"CCB\")")
	@Param(type = {"String"}, name = {"bankName"}, desc = { "银行名称" })
	@Return(type = "String", desc = "根据银行名称获取银行卡号码")
	public static String getBankcardByBankName(String bankName){
		String prefix = bankMap.get(bankName);
		String bankCard = BankCardUtils.getBankCard(prefix);
		return bankCard;
	}

	@Info(name = "getUnUseBankcardByBankName_db", desc = "getUnUseBankcardByBankName_db(\"CCB\")")
	@Param(type = {"String"}, name = {"bankName"}, desc = { "银行名称，如：CCB:建设,CITIC:中信,HXB:华夏,GDB:广发,PAB:平安,ABC:农业,BOC:中国,CMB:招商,CEB:光大,BCOM:民生,SPDB:浦发,ICBC:工商,CIB:兴业,PSBC:邮政"})
	@Return(type = "String", desc = "根据银行名称获取未使用的银行卡")
	public static void getUnUseBankcardByBankName_db(String bankName){
	}
	
	public static String getUnUseBankcardByBankName(String mySql_driver, String mySql_url, String db_name,
			String db_password, String bankName) throws SQLException, Exception{
		String prefix = bankMap.get(bankName);
		String bankCard = BankCardUtils.getBankCard(prefix);
		Connection conn = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		String sql = "select count(*) from user_bankcard_info WHERE bank_card_no='" + bankCard + "'";
		String count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		
		while (Integer.parseInt(count) > 0) {
			bankCard = BankCardUtils.getBankCard(prefix);
			sql = "select count(*) from user_bankcard_info WHERE bank_card_no='" + bankCard + "'";
			count =  String.valueOf(DBUtils.getOneObject(conn, sql));
		}
		DBUtils.closeConnection(conn);
		return bankCard;
	}
	
	@Info(name = "getUsedBankcardByBankName_db", desc = "getUsedBankcardByBankName_db(\"CCB\")")
	@Param(type = {"String"}, name = {"bankName"}, desc = { "银行名称，如：CCB:建设,CITIC:中信,HXB:华夏,GDB:广发,PAB:平安,ABC:农业,BOC:中国,CMB:招商,CEB:光大,BCOM:民生,SPDB:浦发,ICBC:工商,CIB:兴业,PSBC:邮政" })
	@Return(type = "String", desc = "根据银行名称获取已使用的银行卡")
	public static void getUsedBankcardByBankName_db(String bankName){
	}
	
	public static String getUsedBankcardByBankName(String mySql_driver, String mySql_url, String db_name,
		String db_password, String bankName) throws SQLException, Exception{
		String prefix = bankMap.get(bankName);
		Connection conn = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		String sql = "select bank_card_no from user_bankcard_info WHERE bank_card_no like '" + prefix + "%' order by rand() limit 1";
		Object obj = DBUtils.getOneObject(conn, sql);
		String bankCard = String.valueOf(obj);
		return bankCard;
	}

	@Info(name = "getUseBankcardRandom_db", desc = "getUseBankcardRandom_db()")
	@Param(type = {  }, name = {  }, desc = { })
	@Return(type = "String", desc = "随机获取已经存在的银行卡号码")
	public static void getUseBankcardRandom_db(){
	}
	
	public static String getUseBankcardRandom(String mySql_driver, String mySql_url, String db_name,
			String db_password) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		String sql = "SELECT bank_card_no FROM user_bankcard_info where bank_card_no is not null order by rand() LIMIT 1;";
		String bankCard =  String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return bankCard;
	}

	@Info(name = "getBankcardByUserId_db", desc = "getBankcardByUserId_db(\"123\")")
	@Param(type = {"String"}, name = {"userId"}, desc = {"用户id"})
	@Return(type = "String", desc = "根据userId获得用户的银行卡号码")
	public static void getBankcardByUserId_db(String userId){
	}
	
	public static String getBankcardByUserId(String mySql_driver, String mySql_url, String db_name, String db_password,
			String userId) throws SQLException, Exception{
		Connection conn = DBUtils.getConnection(mySql_driver, mySql_url, db_name, db_password);
		String sql = "select bank_card_no from user_bankcard_info where user_id='" + userId + "'";
		String bankcard = String.valueOf(DBUtils.getOneObject(conn, sql));
		DBUtils.closeConnection(conn);
		return bankcard;
	}
	
	public static void main(String [] args) throws Exception{
		try {
			for(int i =0;i<1000;i++){
			String str = MobileUtil.getUnRegisterMobile("com.mysql.jdbc.Driver","jdbc:mysql://192.168.4.13:3306/db_nono?autoReconnect=true", "tester", "r5emEot%");
	
			System.out.println(str);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
