package com.nonobank.testcase.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.DBCfg;
import com.nonobank.testcase.entity.DBGroup;
import com.nonobank.testcase.service.DBCfgService;
import com.nonobank.testcase.service.DBGroupService;

@Controller
@RequestMapping(value="db")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DBController {

	public static Logger logger = LoggerFactory.getLogger(DBController.class);
	
	@Autowired
	DBGroupService dbGroupService;
	
	@Autowired
	DBCfgService dbCfgService;
	
	@GetMapping(value="getAllDBGroup")
	@ResponseBody
	public Result getAllDBGroup(){
		logger.info("开始查找所有数据库分组");
	
		List<DBGroup> dbGroups = dbGroupService.findAll();
		return ResultUtil.success(dbGroups);
	}
	
	@PostMapping(value="addDBGroup")
	@ResponseBody
	public Result addDBGroup(@RequestBody DBGroup dbGroup){
		logger.info("开始保存数据库分组");
		
		if(dbGroup.getId() == null){
			if(dbGroupService.findByGroupName(dbGroup.getGroupName()) != null){
				return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "数据库分组名已存在");
			}else{
				dbGroup.setOptstatus((short)0);
			}
		}
		
		dbGroup = dbGroupService.add(dbGroup);
		return ResultUtil.success(dbGroup);
	}
	
	@PostMapping(value="delDBGroup")
	@ResponseBody
	public Result delDBGroup(@RequestBody DBGroup dbGroup){
		logger.info("开始删除数据库分组");
		
		if(dbCfgService.findByDbGroupIdAndName(dbGroup.getId(), dbGroup.getGroupName()) != null){
			return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "数据库分组已配置了地址，不能删除！");
		}
		
		dbGroupService.delete(dbGroup);
		return ResultUtil.success();
	}
	
	@PostMapping(value="addDBCfg")
	@ResponseBody
	public Result addDBCfg(@RequestBody DBCfg dbCfg){
		logger.info("开始保存数据库地址配置");
		
		if(dbCfg.getId() == null){
			if(dbCfgService.findByDbGroupIdAndName(dbCfg.getDbGroup().getId(), dbCfg.getName()) != null){
				return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "数据库分组配置已存在");
			}
		}
		
		dbCfg = dbCfgService.add(dbCfg);
		return ResultUtil.success(dbCfg);
	}
	
	@PostMapping(value="delDBCfg")
	@ResponseBody
	public Result delDBCfg(@RequestBody DBCfg dbCfg){
		logger.info("开始删除数据库地址配置");
		
		dbCfgService.delete(dbCfg);
		return ResultUtil.success();
	}

	@GetMapping(value="getAllDBCfg")
	@ResponseBody
	public Result getAllDBCfg(){
		logger.info("开始查找所有数据库配置");
		
		List<DBCfg> dbCfgs = dbCfgService.findAllDBCfg();
		return ResultUtil.success(dbCfgs);
	}
	
}
