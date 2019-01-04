package com.nonobank.testcase.remotecontroller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nonobank.testcase.component.result.Result;

@FeignClient(value="PLATFORM-TESTGROUP")
public interface RemoteGroup {

	@GetMapping(value="isCaseInGroup")
    @ResponseBody
    public Result isCaseInGroup(@RequestParam(value="caseId") Integer caseId);
}
