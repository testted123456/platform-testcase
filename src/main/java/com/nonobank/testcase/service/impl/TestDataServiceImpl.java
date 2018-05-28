package com.nonobank.testcase.service.impl;

import com.nonobank.testcase.component.dataProvider.common.BankCardUtils;
import com.nonobank.testcase.component.dataProvider.common.IdCardGenerator;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.entity.DBCfg;
import com.nonobank.testcase.service.TestDataService;
import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.IdCardGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TestDataServiceImpl implements TestDataService {

    public static Logger logger = LoggerFactory.getLogger(TestDataServiceImpl.class);

    @Value("${spring.datasource.driver-class-name}")
    String tpMySQLDriver;

    @Value("${spring.datasource.url}")
    String tpMySQLUrl;

    @Value("${spring.datasource.username}")
    String tpMySQLUsermname;

    @Value("${spring.datasource.password}")
    String tpMySQLPassword;

    /**
     *
     * @param env  环境
     * @return 根据env获取数据库配置信息
     */
    private DBCfg getDBCfgByEnv(String env) throws SQLException {
        DBCfg dbCfg = new DBCfg();

        String sql = "select e.name, c.ip, c.port, c.db_name, c.user_name, c.password from env e " +
                "left join dbgroup g on e.db_group_id = g.id " +
                "left join dbcfg c on g.id = c.db_group_id " +
                "where e.name='" + env + "'";
        Connection connection = DBUtils.getConnection(tpMySQLDriver, tpMySQLUrl, tpMySQLUsermname, tpMySQLPassword);
        Object[] items = DBUtils.getOneLine(connection, sql);
        DBUtils.closeConnection(connection);

        if (items.length < 1){
            return null;
        }

        dbCfg.setIp(String.valueOf(items[1]));
        dbCfg.setPort(String.valueOf(items[2]));
        dbCfg.setDbName(String.valueOf(items[3]));
        dbCfg.setUserName(String.valueOf(items[4]));
        dbCfg.setPassword(String.valueOf(items[5]));
        return dbCfg;
    }

    /**
     * @return 获取所有省份(包含直辖市)
     */
    public List<String> getAllProvince(){
        List<String> list = IdCardGeneratorUtil.areaCode.entrySet().stream().filter( e -> {
            return Pattern.compile("^\\d+0{4}$").matcher(String.valueOf(e.getKey())).find();
        }).sorted(new Comparator<Map.Entry<Integer, String>>() {
            @Override
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return  o1.getKey().compareTo(o2.getKey());
            }
        })
        .map( e -> e.getValue())
        .collect(Collectors.toList());

        return list;
    }

    /**
     * @param province 省份(直辖市)
     * @return 省份(直辖市)下属的城市列表
     */
    public List<String> getCityList(String province){

        String provinceCode = IdCardGeneratorUtil.getProvinceCode(province);

        List<String> list = IdCardGeneratorUtil.areaCode.entrySet().stream().filter( e -> {
            String key = String.valueOf(e.getKey());
            //过滤前2位数字匹配省份(直辖市)，后2位为0, 排除省份(直辖市)
            return !key.equals(provinceCode) && Pattern.compile("^" + provinceCode.substring(0,2) + "\\d+0{2}$").matcher(key).find();
        })
        .sorted(new Comparator<Map.Entry<Integer, String>>() {
            @Override
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return  o1.getKey().compareTo(o2.getKey());
            }
        })
        .map(e -> e.getValue())
        .collect(Collectors.toList());

        return list;
    }

    /**
     *
     * @param province 省份(直辖市)
     * @param city 城市
     * @return 省份(直辖市)城市下的区县列表
     */
    public List<String> getDistrictList(String province, String city){

        String cityCode = IdCardGeneratorUtil.getCityCode(province, city);

        List<String> list = IdCardGeneratorUtil.areaCode.entrySet().stream().filter( e -> {
            String key = String.valueOf(e.getKey());
            //过滤前4位数字匹配城市数字代码, 排除城市
            return !key.equals(cityCode) && Pattern.compile("^" + cityCode.substring(0, 4) +"\\d+$").matcher(key).find();
        })
        .sorted(new Comparator<Map.Entry<Integer, String>>() {
            @Override
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return  o1.getKey().compareTo(o2.getKey());
            }
        })
        .map(e -> e.getValue())
        .collect(Collectors.toList());

        return list;
    }

    /**
     *
     * @param env  环境
     * @param isRegistered  注册 | 未注册
     * @param province  省份(直辖市)
     * @param city  城市
     * @param district  区县
     * @return  身份证号
     * @throws Exception
     */
    public String getIdCardByEnvIsRegisteredProvinceCityDistrict(String env, boolean isRegistered, String province, String city, String district) throws Exception{
        String idCardNum = null;

        DBCfg dbCfg = getDBCfgByEnv(env);
        if (dbCfg == null){
            logger.error(String.format("数据库查询返回为空，请确认环境(%s)数据库是否配置到测试平台", env));
            return null;
        }

        String envMySQLUrl = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8", dbCfg.getIp(), dbCfg.getPort(), dbCfg.getDbName());

        if(isRegistered){
            idCardNum = IdCardGenerator.getRegisterIDCardByProvinceCityDistrict(tpMySQLDriver, envMySQLUrl, dbCfg.getUserName(), dbCfg.getPassword(), province, city, district);
        }else{
            idCardNum = IdCardGenerator.getUnRegisterIDCardByProvinceCityDistrict(tpMySQLDriver, envMySQLUrl, dbCfg.getUserName(), dbCfg.getPassword(), province, city, district);
        }

        return idCardNum;
    }

    public List<String> getAllCNBankName(){
        return BankCardUtils.getCNBankNames();
    }

    public String getBankCardByEnvBanknameIsRegistered(String env, String bankName, boolean isRegistered) throws SQLException, Exception{
        String bankCard = null;

        String enBankName = BankCardUtils.convertCNBankName2EN(bankName);
        if (enBankName == null){
            throw new TestCaseException(10003, "银行名称不匹配，请检查输入是否正确或更新测试平台银行数据");
        }

        DBCfg dbCfg = getDBCfgByEnv(env);
        if (dbCfg == null){
            throw new TestCaseException(10003, String.format("测试平台数据库查询返回为空，请确认环境(%s)数据库是否配置到测试平台", env));
        }

        String envMySQLUrl = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8", dbCfg.getIp(), dbCfg.getPort(), dbCfg.getDbName());
        if(isRegistered){
            bankCard = BankCardUtils.getUsedBankcardByBankName(tpMySQLDriver, envMySQLUrl, dbCfg.getUserName(), dbCfg.getPassword(), enBankName);
            if (bankCard == null || ( bankCard != null && bankCard.toLowerCase().equals("null"))){
                throw new TestCaseException(10003, String.format("测试环境%s数据库不存在已注册%s卡", env, bankName));
            }
        }else{
            bankCard = BankCardUtils.getUnUseBankcardByBankName(tpMySQLDriver, envMySQLUrl, dbCfg.getUserName(), dbCfg.getPassword(), enBankName);
        }
        return bankCard;
    }

}
