package com.nonobank.testcase.service.impl;

import com.nonobank.testcase.component.dataProvider.common.IdCardGenerator;
import com.nonobank.testcase.service.TestDataService;
import com.nonobank.testcase.utils.dll.DBUtils;
import com.nonobank.testcase.utils.dll.IdCardGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
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
     * @param province  省份(直辖市)
     * @return  获取代表省份(直辖市)的数字代码
     */
    private String getProvinceCode(String province){
        Map.Entry<Integer, String> entry = IdCardGeneratorUtil.areaCode.entrySet().stream().filter( e -> {
            return e.getValue().equals(province);
        }).findFirst().get();
        return String.valueOf(entry.getKey());
    }

    /**
     *
     * @param province 省份(直辖市)
     * @param city 城市
     * @return 获取代表省份(直辖市)/城市的数字代码
     */
    private String getCityCode(String province, String city){
        String provinceCode = getProvinceCode(province);

        Map.Entry<Integer, String> entry = IdCardGeneratorUtil.areaCode.entrySet().stream().filter( e -> {
            String key = String.valueOf(e.getKey());
            return e.getValue().equals(city) && Pattern.compile("^" + provinceCode.substring(0,2) + "\\d+0{2}$").matcher(key).find();
        }).findFirst().get();
        return String.valueOf(entry.getKey());

    }


    /**
     *根据环境，是否注册获取身份证号
     * @param env 环境
     * @param isRegistered 注册 | 未注册
     * @return 身份证号
     * @throws Exception
     */
    public String getIdCardByEnvIsRegistered(String env, boolean isRegistered) throws Exception {
        String idCardNum = null;

        logger.info("根据env获取数据库配置信息");
        String sql = "select e.name, c.ip, c.port, c.db_name, c.user_name, c.password from env e " +
                "left join dbgroup g on e.db_group_id = g.id " +
                "left join dbcfg c on g.id = c.db_group_id " +
                "where e.name='" + env + "'";
        Connection connection = DBUtils.getConnection(tpMySQLDriver, tpMySQLUrl, tpMySQLUsermname, tpMySQLPassword);
        Object[] items = DBUtils.getOneLine(connection, sql);
        DBUtils.closeConnection(connection);

        if (items.length < 1){
            logger.error(String.format("数据库查询返回为空，请确认环境(%s)数据库是否配置到测试平台", env));
            return null;
        }
        String envDbIp = String.valueOf(items[1]);
        String envDBPort = String.valueOf(items[2]);
        String envDBName = String.valueOf(items[3]);
        String envDBUsername = String.valueOf(items[4]);
        String envDBPassword = String.valueOf(items[5]);
        String envMySQLUrl = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8", envDbIp, envDBPort, envDBName);

        if (isRegistered){
            logger.info(String.format("获取环境%s已注册身份证号",env));
            idCardNum = IdCardGenerator.getRegisterIDCardRandom(tpMySQLDriver, envMySQLUrl, envDBUsername, envDBPassword);
        }else{
            logger.info(String.format("获取环境%s未注册身份证号", env));
            idCardNum = IdCardGenerator.getUnRegisterIDCard(tpMySQLDriver, envMySQLUrl, envDBUsername, envDBPassword);
        }

        return idCardNum;
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
     *
     * @param province 省份(直辖市)
     * @return 省份(直辖市)下属的城市列表
     */
    public List<String> getCityList(String province){

        String provinceCode = getProvinceCode(province);

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

        String cityCode = getCityCode(province, city);

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

}
