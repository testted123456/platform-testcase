package com.nonobank.testcase.controller;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.service.TestDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping(value="testData")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestDataController {

    public static Logger logger = LoggerFactory.getLogger(TestDataController.class);

    @Autowired
    TestDataService testDataService;

    @GetMapping(value="getAllProvince")
    @ResponseBody
    public Result getAllProvince(){
        List<String> provinces = testDataService.getAllProvince();
        return ResultUtil.success(provinces);
    }

    /**
     *
     * @param reqJson
     * e.g.
     * {
     *     "province":"北京市"
     * }
     * @return
     */
    @PostMapping(value="getCityList")
    @ResponseBody
    public Result getCityList(@RequestBody JSONObject reqJson){
        String province = null;

        logger.info("检查请求参数province");
        if (reqJson.containsKey("province")) {
            province = reqJson.getString("province");
            if (province.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供省份(直辖市)参数为空");
            }

        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供省份(直辖市)参数province");
        }

        List<String> citys = testDataService.getCityList(province);
        return ResultUtil.success(citys);
    }

    /**
     *
     * @param reqJson
     * {
     *     "province":"江苏省",
     *     "city":"南京市"
     * }
     * @return
     */
    @PostMapping(value="getDistrictList")
    @ResponseBody
    public Result getDistrictList(@RequestBody JSONObject reqJson){

        String province = null;
        String city = null;

        logger.info("检查请求参数province");
        if (reqJson.containsKey("province")) {
            province = reqJson.getString("province");
            if (province.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供省份(直辖市)参数为空");
            }

        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供省份(直辖市)参数province");
        }

        logger.info("检查请求参数city");
        if (reqJson.containsKey("city")) {
            city = reqJson.getString("city");
            if (city.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供城市参数为空");
            }

        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供城市参数city");
        }

        List<String> districts = testDataService.getDistrictList(province, city);
        return ResultUtil.success(districts);

    }


    /**
     *
     * @param reqJson
     * e.g.
     * {
     *     "env":"STB",  //必选参数
     *     "isRegistered":true,  //必选参数
     *     "province":"江苏省",  //可选参数
     *     "city":"南京市",  //可选参数
     *     "district":"玄武区"  //可选参数
     * }
     * 注:
     * province为空，则city和district无效
     * province不为空，city为空，则district无效
     * @return 身份证号
     * @throws Exception
     */
    @PostMapping(value="getIdCard")
    @ResponseBody
    public Result getIdCard(@RequestBody JSONObject reqJson) throws Exception {
        String env = null;
        Boolean isRegistered = false;
        String province = null;
        String city = null;
        String district = null;

        logger.info("检查请求参数env -- 必选");
        if (reqJson.containsKey("env")) {
            env = reqJson.getString("env");
            if (env.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供环境参数为空");
            }
        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供环境参数env");
        }

        logger.info("检查请求参数isRegistered -- 必选");
        if (reqJson.containsKey("isRegistered")) {
            isRegistered = reqJson.getBooleanValue("isRegistered");
        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供是否注册参数isRegistered");
        }

        if (reqJson.containsKey("province")) {
            province = reqJson.getString("province");
        }

        if (reqJson.containsKey("city")) {
            city = reqJson.getString("city");
        }

        if (reqJson.containsKey("district")) {
            district = reqJson.getString("district");
        }

        String idCardNum = testDataService.getIdCardByEnvIsRegisteredProvinceCityDistrict(env, isRegistered, province, city, district);
        return ResultUtil.success(idCardNum);
    }

    /**
     * @return 返回所有中文银行名列表
     */
    @GetMapping(value="getAllCNBankName")
    @ResponseBody
    public Result getAllCNBankName(){
        List<String> cnBankNames = testDataService.getAllCNBankName();
        return ResultUtil.success(cnBankNames);
    }

    /**
     * @param reqJson
     * e.g.
     * {
     *     "env": "STB",  //必选参数
     *     "bankname": "广发银行",  //必选参数
     *     "isRegistered": true  //必选参数
     * }
     * @return  根据指定的环境，中文银行名和是否注册，返回银行卡号
     * @throws SQLException
     * @throws Exception
     */
    @PostMapping(value="getBankCard")
    @ResponseBody
    public Result getBankCard(@RequestBody JSONObject reqJson) throws SQLException, Exception {

        String env = null;
        Boolean isRegistered = false;
        String bankname = null;

        logger.info("检查请求参数env -- 必选");
        if (reqJson.containsKey("env")) {
            env = reqJson.getString("env");
            if (env.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供环境参数env为空");
            }
        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供环境参数env");
        }

        logger.info("检查请求参数bankname -- 必选");
        if (reqJson.containsKey("bankname")) {
            bankname = reqJson.getString("bankname");
            if (bankname.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供银行参数bankname为空");
            }
        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供银行参数bankname");
        }

        logger.info("检查请求参数isRegistered -- 必选");
        if (reqJson.containsKey("isRegistered")) {
            isRegistered = reqJson.getBooleanValue("isRegistered");
        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供是否注册参数isRegistered");
        }

        String bankcard = testDataService.getBankCardByEnvBanknameIsRegistered(env, bankname, isRegistered);
        return ResultUtil.success(bankcard);
    }

}
