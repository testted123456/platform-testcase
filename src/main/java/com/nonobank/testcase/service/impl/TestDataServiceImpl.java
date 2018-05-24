package com.nonobank.testcase.service.impl;

import com.nonobank.testcase.component.dataProvider.common.IdCardGenerator;
import com.nonobank.testcase.service.TestDataService;
import com.nonobank.testcase.utils.dll.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;

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

}
