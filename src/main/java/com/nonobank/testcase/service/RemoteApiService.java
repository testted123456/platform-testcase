package com.nonobank.testcase.service;

import java.util.List;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface RemoteApiService {

	JSONObject getApi(Integer id);
	
	JSONObject getLastApi(String system, String module, String branch, String urlAddress);
	
	JSONArray getApisById(List<Integer> ids);
	
	JSONArray getBranchs(String system, String gitAddress);
}
