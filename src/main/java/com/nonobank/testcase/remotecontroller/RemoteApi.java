package com.nonobank.testcase.remotecontroller;

import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.result.Result;

@FeignClient(value="PLATFORM-INTERFACE")
public interface RemoteApi {

	@GetMapping(value="api/getApi")
	@ResponseBody
	Result getApi(@RequestParam(value = "id") Integer id);
	
	@GetMapping(value="api/getLastApis")
	@ResponseBody
	Result getLastApi(@RequestParam(value = "system") String system, @RequestParam(value = "module") String module, @RequestParam(value = "urlAddress") String urlAddress, @RequestParam(value = "branch") String branch);

	@PostMapping(value="api/findByIds", consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Result findByIds(@RequestBody List<Integer> ids);
	
	@PostMapping(value="api/getBranchs", consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Result getBranchs(@RequestBody JSONObject jsonObj);

}
