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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="testData")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestDataController {

    public static Logger logger = LoggerFactory.getLogger(TestDataController.class);

    @Autowired
    TestDataService testDataService;

    /**
     *
     * @param reqJson
     * e.g.
     * {
     *     "env":"STB",
     *     "isRegistered":true
     * }
     * env: 环境
     * isRegistered: 注册| 未注册
     * @return
     * @throws Exception
     */
    @PostMapping(value="getIdCard")
    @ResponseBody
    public Result getIdCard(@RequestBody JSONObject reqJson) throws Exception {
        String env = null;
        Boolean isRegistered = false;

        logger.info("检查请求参数env");
        if (reqJson.containsKey("env")) {
            env = reqJson.getString("env");
            if (env.isEmpty()){
                return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求提供环境参数为空");
            }

        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供环境参数env");
        }

        logger.info("检查请求参数isRegistered");
        if (reqJson.containsKey("isRegistered")) {
            isRegistered = reqJson.getBooleanValue("isRegistered");
        }else{
            return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "请求未提供是否注册参数isRegistered");
        }

        String idCardNum = testDataService.getIdCardByEnvIsRegistered(env, isRegistered);
        return ResultUtil.success(idCardNum);
    }

}
